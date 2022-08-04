package fr.test.test.compute.config.mongodb;

import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongoCmdOptions;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.Feature;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.distribution.Versions;
import de.flapdoodle.embed.process.runtime.Network;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@AutoConfigureBefore(EmbeddedMongoAutoConfiguration.class)
@EnableConfigurationProperties({MongoProperties.class, EmbeddedMongoProperties.class})
@RequiredArgsConstructor
public class EmbeddedMongoConfigOverride {

    private static final byte[] IP4_LOOPBACK_ADDRESS = {127, 0, 0, 1};
    private static final byte[] IP6_LOOPBACK_ADDRESS = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
    private final MongoProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public MongodConfig embeddedMongoConfiguration(EmbeddedMongoProperties embeddedProperties) throws IOException {
        ImmutableMongodConfig.Builder builder = MongodConfig.builder().version(determineVersion(embeddedProperties));
        var storage = embeddedProperties.getStorage();
        if (storage != null) {
            String databaseDir = storage.getDatabaseDir();
            String replSetName = storage.getReplSetName();
            int oplogSize = (storage.getOplogSize() != null) ? (int) storage.getOplogSize().toMegabytes() : 0;
            builder.replication(new Storage(databaseDir, replSetName, oplogSize));
            builder.cmdOptions(MongoCmdOptions.builder().useNoJournal(false).build());
        }
        Integer configuredPort = this.properties.getPort();
        if (configuredPort != null && configuredPort > 0) {
            builder.net(new Net(getHost().getHostAddress(), configuredPort, Network.localhostIsIPv6()));
        } else {
            builder.net(new Net(getHost().getHostAddress(), Network.getFreeServerPort(getHost()),
                                Network.localhostIsIPv6()));
        }
        return builder.build();
    }

    private IFeatureAwareVersion determineVersion(EmbeddedMongoProperties embeddedProperties) {
        Assert.state(embeddedProperties.getVersion() != null, "Set the spring.mongodb.embedded.version property or " +
                "define your own MongodConfig bean to use embedded MongoDB");
        if (embeddedProperties.getFeatures() == null) {
            for (Version version : Version.values()) {
                if (version.asInDownloadPath().equals(embeddedProperties.getVersion())) {
                    return version;
                }
            }
            return Versions.withFeatures(createEmbeddedMongoVersion(embeddedProperties));
        }
        return Versions.withFeatures(createEmbeddedMongoVersion(embeddedProperties),
                                     embeddedProperties.getFeatures().toArray(new Feature[0]));
    }

    private de.flapdoodle.embed.process.distribution.Version.GenericVersion createEmbeddedMongoVersion(
            EmbeddedMongoProperties embeddedProperties) {
        return de.flapdoodle.embed.process.distribution.Version.of(embeddedProperties.getVersion());
    }

    private InetAddress getHost() throws UnknownHostException {
        if (this.properties.getHost() == null) {
            return InetAddress.getByAddress(Network.localhostIsIPv6() ? IP6_LOOPBACK_ADDRESS : IP4_LOOPBACK_ADDRESS);
        }
        return InetAddress.getByName(this.properties.getHost());
    }
}
