package org.ecsoya.fabric;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;

public class FabcarChaincode extends ChaincodeBase {
	private static Log logger = LogFactory.getLog(FabcarChaincode.class);

	@Override
	public Response init(ChaincodeStub stub) {
		logger.info("Initialize Fabcar chaincode");
		return newSuccessResponse();
	}

	@Override
	public Response invoke(ChaincodeStub stub) {
		try {
			logger.info("Invoke java fabcar chaincode");
			String func = stub.getFunction();

			if (func.equals("queryCar")) {
				return queryCar(stub);
			} else if (func.equals("createCar")) {
				return createCar(stub);
			} else if (func.equals("queryAllCars")) {
				return queryAllCars(stub);
			} else if (func.equals("changeCarOwner")) {
				return changeCarOwner(stub);
			}
			return newErrorResponse(
					"Invalid invoke function name. Expecting one of: [\"invoke\", \"delete\", \"query\"]");
		} catch (Throwable e) {
			return newErrorResponse(e);
		}
	}

	private Response changeCarOwner(ChaincodeStub stub) {
		List<String> params = stub.getParameters();
		if (params.size() != 2) {
			return newErrorResponse("Incorrect number of arguments. Excepting 2");
		}
		String key = params.get(0);
		String newOwner = params.get(1);
		byte[] carBytes = stub.getState(key);

		return newSuccessResponse("Owner changed");
	}

	private Response queryAllCars(ChaincodeStub stub) {
		String startKey = "CAR0";
		String endKey = "CAR999";

		final StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		boolean alreadyWritten = false;
		Iterator<KeyValue> iterator = stub.getStateByRange(startKey, endKey).iterator();
		while (iterator.hasNext()) {
			KeyValue keyValue = iterator.next();
			if (alreadyWritten) {
				buffer.append(",");
			}
			buffer.append("{");
			buffer.append("\"Key\":");
			buffer.append("\"");
			buffer.append(keyValue.getKey());
			buffer.append("\"");
			buffer.append(",");
			buffer.append("\"Record\":");
			buffer.append(keyValue.getStringValue());
			buffer.append("}");
			alreadyWritten = true;
		}
		buffer.append("]");

		return newSuccessResponse(buffer.toString().getBytes());
	}

	private Response createCar(ChaincodeStub stub) {
		List<String> params = stub.getParameters();
		return null;
	}

	private Response queryCar(ChaincodeStub stub) {
		List<String> params = stub.getParameters();
		if (params.size() != 1) {
			return newErrorResponse("Incorrect number of arguments. Expecting 1");
		}
		byte[] state = stub.getState(params.get(0));
		return newSuccessResponse(state);
	}

	public static void main(String[] args) {
		new FabcarChaincode().start(args);
	}
}
