#Java Chaincode

1. Please use 'maven-shade-plugin' to build a fat jar.

```
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-shade-plugin</artifactId>
				<version>3.1.0</version>
				<executions>
					<execution>
						<phase>package</phase>
						<goals>
							<goal>shade</goal>
						</goals>
						<configuration>
							<finalName>chaincode</finalName>
							<transformers>
								<transformer
									implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
									<mainClass>org.ecsoya.fabric.FabcarChaincode</mainClass>
								</transformer>
							</transformers>
							<filters>
								<filter>
									<!-- filter out signature files from signed dependencies, else repackaging 
										fails with security ex -->
									<artifact>*:*</artifact>
									<excludes>
										<exclude>META-INF/*.SF</exclude>
										<exclude>META-INF/*.DSA</exclude>
										<exclude>META-INF/*.RSA</exclude>
									</excludes>
								</filter>
							</filters>
						</configuration>
					</execution>
				</executions>
			</plugin>
```

2. The jar name should be 'chaincode.jar'.

```
<finalName>chaincode</finalName>
```

3. Make sure to to 'mvn clean' on local project, the jar will be built by fabric on the docker.

4. Error: 

```
2019-06-13 09:38:46.763 UTC [endorser] SimulateProposal -> ERRO 067 [mychannel][42ac6270] failed to invoke chaincode name:"lscc" , error: container exited with 1
github.com/hyperledger/fabric/core/chaincode.(*RuntimeLauncher).Launch.func1
	/opt/gopath/src/github.com/hyperledger/fabric/core/chaincode/runtime_launcher.go:63
runtime.goexit
	/opt/go/src/runtime/asm_amd64.s:1333
chaincode registration failed
```

The main method was not added to Chaincode class.

```
	public static void main(String[] args) {
		new FabcarChaincode().start(args);
	}
```