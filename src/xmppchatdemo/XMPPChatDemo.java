/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppchatdemo;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;

/**
 *
 * @author AYO
 */
public class XMPPChatDemo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here       
        new Thread(){
            public void run(){
                XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword("james", "123456")
                .setHost("localhost") 
                .setServiceName("localhost")
                .setPort(5222)
                .build();
        
                AbstractXMPPConnection con = new XMPPTCPConnection(config);
                try {
                    con.connect();
                    con.login();
                    //System.out.println(con.isConnected()+" "+con.isAnonymous()+" "+con.isAuthenticated()+" "+con.isSecureConnection()+" "+con.isUsingCompression());
                    
                    ChatManager chatManager = ChatManager.getInstanceFor(con);
                    EntityBareJid jid = JidCreate.entityBareFrom("ay@localhost");
                    
                    Chat chat = chatManager.createChat(jid.asEntityBareJidString());
                                       
                    chatManager.addChatListener(new ChatManagerListener() {
                        @Override
                        public void chatCreated(Chat chat, boolean createdLocally) {
                            if (!createdLocally)
				chat.addMessageListener(new ChatMessageListener() {
                                @Override
                                public void processMessage(Chat chat, Message message) {
                                    System.out.println("Received message: " + message.getBody());
                                }
                                });
                        }
                    });
                    
//                    AccountManager acct = AccountManager.getInstance(con);
//                    acct.sensitiveOperationOverInsecureConnection(true);
//                    acct.createAccount("test", "123456");                    
//                    System.out.println(acct.supportsAccountCreation());
                    
                    Scanner in = new Scanner(System.in);
                    while(con.isConnected()){
                        System.out.print("Send Message: ");
                        chat.sendMessage(in.nextLine());
                    }
//                    Chat chat = chatManager.createChat("james@localhost", new MessageListener() {
//                        @Override
//                        public void processMessage(Message message) {
//                            System.out.println("Received message: " + message);
//                        }
//                    }) ;
//                       
//                    chat.sendMessage("Howdy!");
//                    

                } catch (SmackException ex) {
                    Logger.getLogger(XMPPChatDemo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (XMPPException ex) {
                    Logger.getLogger(XMPPChatDemo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(XMPPChatDemo.class.getName()).log(Level.SEVERE, null, ex);
                }             
            }
        }.start();
    }
    
}
