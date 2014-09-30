package com.tridion.storage.extensions;

import com.tridion.configuration.ConfigurationException;
import com.tridion.deployer.Deployer;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * User: ray
 * Date: 01/08/2011
 * Time: 14:39
 */
public class TestDeployer extends CamelTestSupport {

    @Test
    public void testDeployer() throws ConfigurationException {

        Deployer testDeployer = Deployer.getInstance();

        System.out.println("License License Product Name: " + testDeployer.getLicenseProductName());
        System.out.println("License Manifest URL: " + testDeployer.getManifestURL());
        System.out.println("License Product Name: " + testDeployer.getProductName());

        testDeployer.start();

        try {
            Thread.sleep((long) 2000000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


}
