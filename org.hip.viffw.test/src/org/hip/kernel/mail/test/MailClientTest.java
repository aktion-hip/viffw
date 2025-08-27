package org.hip.kernel.mail.test;

import javax.mail.internet.InternetAddress;

import org.hip.kernel.mail.MailClient;
import org.hip.kernel.mail.VMultiPartMessage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class MailClientTest {

    @Test
    @Disabled("Problem with com.sun.mail.util.PropUtil")
    void testSendMail() throws Exception {
        final String lTo = "lbenno@openlu.ethz.ch";
        final String lFrom = "benno.luthiger@id.ethz.ch";
        //		String lBody = "Testmail von James";
        final String lBodyHtml = "Testmail f�r James</br>Bitte <a href=\"http://www.aktion-hip.ch/\">antworten</a>";
        final String lSubject = "[VIF] Mail Test 4";
        //			InternetAddress lAddress = new InternetAddress();
        final InternetAddress lAddressFrom = new InternetAddress(lFrom);
        final VMultiPartMessage lMessage = new VMultiPartMessage();
        lMessage.setSubject(lSubject);
        lMessage.setToAddresses(InternetAddress.parse(lTo));
        //			lMessage.addPart(lBody);
        lMessage.addHtmlPart(lBodyHtml);
        lMessage.setFromAddress(lAddressFrom);
        final MailClient lMailer = new MailClient();
        lMailer.sendMail(lMessage);
        System.out.println("Testmail sent.");
    }
}
