package com.tridion.storage.extensions;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.tridion.configuration.Configuration;
import com.tridion.deployer.Deployer;
import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.config.store.HttpProxyFactory;
import de.flapdoodle.embed.process.runtime.Network;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Created by AEY5753 on 26/05/2015.
 */
public class UnitMongoTest extends TestCase {


    /**
     * please store Starter or RuntimeConfig in a static final field
     * if you want to use artifact store caching (or else disable caching)
     */

    Command command = Command.MongoD;

    // Work around TME Bluecoat limitations
    IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()
        .defaults(command)
        .artifactStore(new ArtifactStoreBuilder()
                .defaults(command)
                .download(new DownloadConfigBuilder()
                        .defaultsForCommand(command)))
                //.proxyFactory(new HttpProxyFactory("10.101.110.47", 3128))))
            .build();

    //TODO: How to temporarily configure the test folders / load test XML ?
    Configuration configuration = new Configuration("toto");

    private MongodStarter starter = MongodStarter.getInstance(runtimeConfig);
    private Deployer deployer = Deployer.getInstance();

    private MongodExecutable _mongodExe;
    private MongodProcess _mongod;

    private MongoClient _mongo;

    @Override
    protected void setUp() throws Exception {

        System.out.println("Config troubleshoot: " );
        //deployer.configure(configuration);
        deployer.start();
        _mongodExe = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(12345, Network.localhostIsIPv6()))
                .build());
        _mongod = _mongodExe.start();

        super.setUp();
    }

    @Test
    public void testMongo() throws IOException {

        URL url = getClass().getResource("/test-files/zip/tcm_0-157-66560.Content.zip");
        File sourceFile = new File(url.getPath());

        System.out.println("file path: " + url.getPath());

        FileSystem defaultFS = FileSystems.getDefault();

        Path sourcePath = defaultFS.getPath("/test-files/zip/tcm_0-157-66560.Content.zip");
        Path destPath  = defaultFS.getPath("pets/charm_harold.xml");

        File destinationFile = new File("C:\\Data\\tridion\\poc-incoming\\" + sourceFile.getName());
        File successFile = new File("C:\\Data\\tridion\\poc-incoming\\Success\\" + sourceFile.getName());

        try {
            Files.copy(sourceFile.toPath(), destinationFile.toPath());
            //copyFileUsingStream(sourceFile, destinationFile);
            System.out.println("File copied, check deployer");
            Thread.sleep(20000);
            System.out.println("Waiting is over, something happened ?");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(successFile.exists());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        deployer.stop();
        _mongod.stop();
        _mongodExe.stop();
    }

    public Mongo getMongo() {
        return _mongo;
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

}