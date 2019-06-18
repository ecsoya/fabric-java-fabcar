package org.ecsoya.fabric;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.google.protobuf.ByteString;

public class FabcarChaincode extends ChaincodeBase {
	private static Log logger = LogFactory.getLog(FabcarChaincode.class);

	@Override
	public Response init(ChaincodeStub stub) {
		logger.info("Chaincode Fabcar: Initialize Fabcar chaincode");
		createCar(stub);
		return newSuccessResponse();
	}

	@Override
	public Response invoke(ChaincodeStub stub) {
		try {
			logger.info("Chaincode Fabcar: Invoke java fabcar chaincode");
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
					"Chaincode Fabcar: Invalid invoke function name. Expecting one of: [\"queryCar\", \"createCar\", \"queryAllCars\", \\\"changeCarOwner\\\"]");
		} catch (Throwable e) {
			return newErrorResponse(e);
		}
	}

	private Response changeCarOwner(ChaincodeStub stub) {
		logger.info("Chaincode Fabcar: changeCarOwner");
		List<String> params = stub.getParameters();
		if (params.size() != 2) {
			return newErrorResponse("Chaincode Fabcar: Incorrect number of arguments. Excepting 2");
		}
		String key = params.get(0);
		String newOwner = params.get(1);
		String json = stub.getStringState(key);
		if (json == null || json.equals("")) {
			return newErrorResponse("Chaincode Fabcar: failed to change car owner, car not exist for " + key);
		}
		Car car = Car.fromJSON(json);
		car.setOwner(newOwner);
		stub.putStringState(key, car.toString());

		return newSuccessResponse("Owner changed");
	}

	private Response queryAllCars(ChaincodeStub stub) {
		String startKey = "CAR0";
		String endKey = "CAR999";
		List<String> parameters = stub.getParameters();
		if (parameters.size() > 0) {
			startKey = parameters.get(0);
		}
		if (parameters.size() > 1) {
			endKey = parameters.get(1);
		}
		logger.info("Chaincode Fabcar: queryAllCars from " + startKey + " to " + endKey);

		final StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		boolean alreadyWritten = false;
		QueryResultsIterator<KeyValue> stateByRange = stub.getStateByRange(startKey, endKey);
		Iterator<KeyValue> iterator = stateByRange.iterator();
		while (iterator.hasNext()) {
			KeyValue keyValue = iterator.next();
			if (alreadyWritten) {
				buffer.append(",");
			}
			buffer.append("{");
			buffer.append("\"key\":");
			buffer.append("\"");
			buffer.append(keyValue.getKey());
			buffer.append("\"");
			buffer.append(",");
			buffer.append("\"car\":");
			buffer.append(keyValue.getStringValue());
			buffer.append("}");
			alreadyWritten = true;
		}
		buffer.append("]");
		String value = buffer.toString();
		return newSuccessResponse(value, ByteString.copyFrom(value, UTF_8).toByteArray());
	}

	private Response createCar(ChaincodeStub stub) {
		List<String> params = stub.getParameters();
		if (params.size() < 2) {
			return newErrorResponse("Chaincode Fabcar: Incorrect number of arguments. Expecting 2 (key, car)");
		} else if (params.size() == 2) {
			String key = params.get(0);
			String carState = stub.getStringState(key);
			if (carState != null && !carState.equals("")) {
				return newErrorResponse("Chaincode Fabcar: Car for key \"" + key + "\" is already exist");
			}
			logger.info("Chaincode Fabcar: createCar " + Arrays.toString(params.toArray()));
			String carStr = params.get(1);
			stub.putStringState(key, carStr);
		} else if (params.size() > 2) {
			String key = params.get(0);
			String make = params.get(1);
			String model = params.get(2);
			String color = "";
			String owner = "";
			if (params.size() > 3) {
				color = params.get(3);
			}
			if (params.size() > 4) {
				owner = params.get(4);
			}
			Car car = new Car();
			car.setColor(color);
			car.setMake(make);
			car.setModel(model);
			car.setOwner(owner);
			stub.putStringState(key, car.toString());
		}
		return newSuccessResponse();
	}

	private Response queryCar(ChaincodeStub stub) {
		List<String> params = stub.getParameters();
		if (params.size() != 1) {
			return newErrorResponse("Chaincode Fabcar: Incorrect number of arguments. Expecting 1");
		}
		String key = params.get(0);

		logger.info("Chaincode Fabcar: queryCar for " + key);
		String car = stub.getStringState(key);
		return newSuccessResponse(car, ByteString.copyFrom(car, UTF_8).toByteArray());
	}

	public static void main(String[] args) {
		new FabcarChaincode().start(args);
	}
}
