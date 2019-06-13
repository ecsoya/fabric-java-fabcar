/****************************************************** 
 *  Copyright 2018 IBM Corporation 
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at 
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *  See the License for the specific language governing permissions and 
 *  limitations under the License.
 */
package org.ecsoya.fabric.network;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ecsoya.fabric.client.ChannelClient;
import org.ecsoya.fabric.client.FabricClient;
import org.ecsoya.fabric.config.Config;
import org.ecsoya.fabric.user.UserContext;
import org.ecsoya.fabric.util.Util;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

/**
 * 
 * @author Balaji Kadambi
 *
 */

public class DeployInstantiateChaincode {

	public static void main(String[] args) {
		try {
			CryptoSuite.Factory.getCryptoSuite();

			UserContext org1Admin = new UserContext();
			File pkFolder1 = new File(Config.ORG1_USR_ADMIN_PK);
			File[] pkFiles1 = pkFolder1.listFiles();
			File certFolder = new File(Config.ORG1_USR_ADMIN_CERT);
			File[] certFiles = certFolder.listFiles();
			Enrollment enrollOrg1Admin = Util.getEnrollment(Config.ORG1_USR_ADMIN_PK, pkFiles1[0].getName(),
					Config.ORG1_USR_ADMIN_CERT, certFiles[0].getName());
			org1Admin.setEnrollment(enrollOrg1Admin);
			org1Admin.setMspId("Org1MSP");
			org1Admin.setName("admin");

			UserContext org2Admin = new UserContext();
			File pkFolder2 = new File(Config.ORG2_USR_ADMIN_PK);
			File[] pkFiles2 = pkFolder2.listFiles();
			File certFolder2 = new File(Config.ORG2_USR_ADMIN_CERT);
			File[] certFiles2 = certFolder2.listFiles();
			Enrollment enrollOrg2Admin = Util.getEnrollment(Config.ORG2_USR_ADMIN_PK, pkFiles2[0].getName(),
					Config.ORG2_USR_ADMIN_CERT, certFiles2[0].getName());
			org2Admin.setEnrollment(enrollOrg2Admin);
			org2Admin.setMspId(Config.ORG2_MSP);
			org2Admin.setName(Config.ADMIN);

			FabricClient fabClient = new FabricClient(org1Admin);

			Channel mychannel = fabClient.getInstance().newChannel(Config.CHANNEL_NAME);
			Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
			Peer peer0_org1 = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
			Peer peer1_org1 = fabClient.getInstance().newPeer(Config.ORG1_PEER_1, Config.ORG1_PEER_1_URL);
			Peer peer0_org2 = fabClient.getInstance().newPeer(Config.ORG2_PEER_0, Config.ORG2_PEER_0_URL);
			Peer peer1_org2 = fabClient.getInstance().newPeer(Config.ORG2_PEER_1, Config.ORG2_PEER_1_URL);
			mychannel.addOrderer(orderer);
			mychannel.addPeer(peer0_org1);
			mychannel.addPeer(peer1_org1);
			mychannel.addPeer(peer0_org2);
			mychannel.addPeer(peer1_org2);
			mychannel.initialize();

			List<Peer> org1Peers = new ArrayList<Peer>();
			org1Peers.add(peer0_org1);
			org1Peers.add(peer1_org1);

			List<Peer> org2Peers = new ArrayList<Peer>();
			org2Peers.add(peer0_org2);
			org2Peers.add(peer1_org2);

			File chaincodeProject = Paths.get(new File("../fabcar-chaincode").getAbsolutePath()).toFile();

			Collection<ProposalResponse> response = fabClient.deployJavaChainCode(Config.CHAINCODE_1_NAME,
					chaincodeProject, Config.CHAINCODE_1_VERSION, org1Peers);

			for (ProposalResponse res : response) {
				Logger.getLogger(DeployInstantiateChaincode.class.getName()).log(Level.INFO,
						Config.CHAINCODE_1_NAME + "- Chain code deployment " + res.getStatus());

				System.out.println(res.getMessage());
			}

			fabClient.getInstance().setUserContext(org2Admin);

			response = fabClient.deployJavaChainCode(Config.CHAINCODE_1_NAME, chaincodeProject,
					Config.CHAINCODE_1_VERSION, org2Peers);

			for (ProposalResponse res : response) {
				Logger.getLogger(DeployInstantiateChaincode.class.getName()).log(Level.INFO,
						Config.CHAINCODE_1_NAME + "- Chain code deployment " + res.getStatus());
				System.out.println(res.getMessage());
			}

			ChannelClient channelClient = new ChannelClient(mychannel.getName(), mychannel, fabClient);

			String[] arguments = { "" };
			response = channelClient.instantiateJavaChainCode(Config.CHAINCODE_1_NAME, Config.CHAINCODE_1_VERSION,
					chaincodeProject.getPath(), "init", arguments, null);

			for (ProposalResponse res : response) {
				Logger.getLogger(DeployInstantiateChaincode.class.getName()).log(Level.INFO,
						Config.CHAINCODE_1_NAME + "- Chain code instantiation " + res.getStatus());
				System.out.println(res.getMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
