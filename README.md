# fabric-java-fabcar
Hyperledger Fabric Demo by using Java Chaincode and Java SDK

#### 一、Hyperledger Fabric 安装

参考 

1. [Hyperledger Fabric Installation](Hyperledger Fabric Installation.md)
2. [Prerequisites](https://hyperledger-fabric.readthedocs.io/en/latest/prereqs.html)
3. [Install Samples, Binaries and Docker Images](https://hyperledger-fabric.readthedocs.io/en/latest/install.html)

#### 二、工程介绍

1. **fabcar-network**：fabric网络配置和启动文件。
2. **fabcar-chaincode**：基于Java实现的Chaincode。
3. **fabcar-app**：fabric应用，基于Java SDK的实现。


#### 三、示例

1. 初始化网络，进入 **fabcar-network**。

   - 运行**generate.sh**命令，生成证书文件以及创世块等。
   
     由于所有的文件都需要在**fabcar-app**中调用，所以生成的文件被保存到了**fabcar-app/src/main/resources**文件夹中：*config*和*crypto-config*
   
   - 运行**start.sh**命令，启动fabric网络。
   
     ```
     docker启动之后：
     4个peer容器
     2个ca容器
     1个orderer容器
     ```
   
   - 运行**stop.sh**命令和**teardown.sh**停止和清除所有的docker容器。
   
     
   
2. fabcar-app/CreateChannel，创建容器

3. fabcar-app/DeployInstantiateChaincode，安装和初始化Chaincode（Java）。

   关于使用Java编写Chaincode的注意事项，请参考fabcar-chaincode工程。

4. fabcar-app/chaincode.invocation，Chaincode的常规使用。