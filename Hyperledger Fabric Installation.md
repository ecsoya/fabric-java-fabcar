# Install Hyperledger Fabric

Operating System：CentOS

### Prerequisites

#### 1. Curl

Already had in CentOS.

#### 2. Docker 

1）Install needed packages

   ```shell
   $ sudo yum install -y yum-utils device-mapper-persistent-data lvm2
   ```

2）Configure the docker-ce repo

   ```shell
   $ sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
   ```

3） Install docker-ce

   ```shell
   $ sudo yum install docker-ce
   ```

4） Set Docker to start automatically at boot time

   ```shell
   $ sudo systemctl enable docker.service
   ```

5）Start the Docker service

   ```shell
   $ sudo systemctl start docker.service
   ```

6) Verify

   ```shell
   $ docker --version
   ```

#### 3. Docker Compose

1). Install Extra Packages for Enterprise Linux

   ```shell
   $ sudo yum install epel-release
   ```

2). Install python-pip

   ```shell
   $ sudo yum install -y python-pip
   ```

3). Then Install Docker Compose

   ```shell
   $ sudo pip install docker-compose
   ```

4). You will also need to upgrade your Python packages on CentOS 7 to get docker-compose to run successfully

   ```shell
   $ sudo yum upgrade python*
   ```

5). Upgrade pip if needed 

You are using pip version 8.1.2, however version 19.1.1 is available.

You should consider upgrading via the 'pip install --upgrade pip' command.

   ```shell
   $ pip install --upgrade pip
   ```

6). Verify

   ```shell
   $ docker-compose version
   ```

#### 4. Go Programming Language

1). Download install file for Linux

   ```
   https://golang.org/doc/install?download=go1.12.6.linux-amd64.tar.gz
   ```

2). If you using a remote server, upload with `scp` command, otherwise using just `cp`command.

   ```shell
   $ scp go1.12.6.linux-amd64.tar.gz root@134.175.132.47:/home/soyatec
   ```

3). Extract the downloaded tarball to */usr/local*

   ```shell
   $ tar -C /usr/local/ -xzf /home/soyatec/go1.12.6.linux-amd64.tar.gz 
   ```

4). Set Go Path

   ```shell
   $ sudo vim /etc/profile
   ```

add following lines at the end of `etc/profile` file.

   ```sh
   export GOROOT=/usr/local/go
   export GOPATH=/home/go
   export PATH=$PATH:$GOROOT/bin:$GOPATH/bin
   ```

> There are 2 paths added: 
>
> 1. `GOROOT/bin` is the binaries of the golang.
> 2. `GOPATH/bin` is the third-part binaries of golang, the binaries of Hyperledger Fabric will be installed in this path later. 

then run the `source` command to applying new env.

   ```shell
   $ source /etc/profile
   ```

5). Verify

   ```shell
   $ go env
   ```

   ```sh
GOARCH="amd64"
GOBIN=""
GOCACHE="/root/.cache/go-build"
GOEXE=""
GOFLAGS=""
GOHOSTARCH="amd64"
GOHOSTOS="linux"
GOOS="linux"
GOPATH="/home/go"
GOPROXY=""
GORACE=""
GOROOT="/usr/local/go"
GOTMPDIR=""
GOTOOLDIR="/usr/local/go/pkg/tool/linux_amd64"
GCCGO="gccgo"
CC="gcc"
CXX="g++"
CGO_ENABLED="1"
GOMOD=""
CGO_CFLAGS="-g -O2"
CGO_CPPFLAGS=""
CGO_CXXFLAGS="-g -O2"
CGO_FFLAGS="-g -O2"
CGO_LDFLAGS="-g -O2"
PKG_CONFIG="pkg-config"
GOGCCFLAGS="-fPIC -m64 -pthread -fmessage-length=0 -fdebug-prefix-map=/tmp/go-build745238100=/tmp/go-build -gno-record-gcc-switches"
   ```

### Install Hyperledger Fabric

1. ##### Using shortened URL and curl command

   ```shell
   $ cd /home/go
   $ curl -sSL http://bit.ly/2ysbOFE | bash -s
   ```

   You can specific the version after `-s` 

   ```sh
   curl -sSL http://bit.ly/2ysbOFE | bash -s -- <fabric_version> <fabric-ca_version> <thirdparty_version>
   $ curl -sSL http://bit.ly/2ysbOFE | bash -s -- 2.0.0-alpha 2.0.0-alpha 0.4.15
   ```

   This may be very slow, you can try the second method.

2. ##### Using the `bootstrap.sh` directly

   1). Download `bootstrap.sh` from URL: 

   ```
   https://raw.githubusercontent.com/hyperledger/fabric/master/scripts/bootstrap.sh
   ```

   2). Copy it to `/home/go` directory, `scp` for remote server, `cp` for local.

   ```shell
   $ scp bootstrap.sh root@134.175.132.47:/home/go
   ```

   3). Set permissions by using `chmod` command

   ```shell
   $ chmod a+x bootstrap.sh 
   ```

   4). run `bootstrap.sh`

   ```shell
   $ ./bootstrap.sh
   ```

   > There are 3 tasks in this bootstrap.sh script
   >
   > a. Clone `fabric-samples` repo
   >
   > b. Download of `platform-specific` binaries
   >
   > c. Download `docker images`
   >

   ```sh
   printHelp() {
       echo "Usage: bootstrap.sh [version [ca_version [thirdparty_version]]] [options]"
       echo
       echo "options:"
       echo "-h : this help"
       echo "-d : bypass docker image download"
       echo "-s : bypass fabric-samples repo clone"
       echo "-b : bypass download of platform-specific binaries"
       echo
       echo "e.g. bootstrap.sh 1.4.1 -s"
       echo "would download docker images and binaries for version 1.4.1"
   }
   ```

   The download may take long time, you can download specific binaries and then untar them to `/home/go`

3. ##### Manually install Fabric Binaries

   1). Download `fabric` binaries
   
   ```
   https://nexus.hyperledger.org/content/repositories/releases/org/hyperledger/fabric/hyperledger-fabric/linux-amd64-1.4.1/hyperledger-fabric-linux-amd64-1.4.1.tar.gz
   ```

   2). Download `fabric-ca` binaries

   ```
   https://nexus.hyperledger.org/content/repositories/releases/org/hyperledger/fabric-ca/hyperledger-fabric-ca/linux-amd64-1.4.1/hyperledger-fabric-ca-linux-amd64-1.4.1.tar.gz
   ```

   3). Copy them to ` /home/go`

   4). Extract these 2 files to `/home/go` directory directly.

   5). Verify

   ```shell
   $ tree
   ```
	> if you do not have `tree` command, run `yum install tree -y` command to install it.
   
   ```sh
   |-- bin
   |   |-- configtxgen
   |   |-- configtxlator
   |   |-- cryptogen
   |   |-- discover
   |   |-- fabric-ca-client
   |   |-- idemixgen
   |   |-- orderer
   |   |-- peer
   |-- bootstrap.sh
   |-- config
       |-- configtx.yaml
       |-- core.yaml
       |-- orderer.yaml
   ```
   
   > The `/home/go/bin` has already added to the PATH when installing Golang, so that we can run the peer command directly here.
   >
   
   ```shell
   $ peer version
   ```
   
   ```sh
   peer:
    Version: 1.4.1
    Commit SHA: 87074a7
    Go version: go1.11.5
    OS/Arch: linux/amd64
    Chaincode:
     Base Image Version: 0.4.15
     Base Docker Namespace: hyperledger
     Base Docker Label: org.hyperledger.fabric
     Docker Namespace: hyperledger
   ```

   6). Install docker images

   ```shell
   $ ./bootstrap.sh -s -b
   ```

   `-s -b` Ignore to download binaries and samples

   7). List of docker images

   ```sh
   ===> List out hyperledger docker images
   hyperledger/fabric-ca          1.4.1               3a1799cda5d7        2 months ago        252MB
   hyperledger/fabric-ca          latest              3a1799cda5d7        2 months ago        252MB
   hyperledger/fabric-tools       1.4.1               432c24764fbb        2 months ago        1.55GB
   hyperledger/fabric-tools       latest              432c24764fbb        2 months ago        1.55GB
   hyperledger/fabric-ccenv       1.4.1               d7433c4b2a1c        2 months ago        1.43GB
   hyperledger/fabric-ccenv       latest              d7433c4b2a1c        2 months ago        1.43GB
   hyperledger/fabric-orderer     1.4.1               ec4ca236d3d4        2 months ago        173MB
   hyperledger/fabric-orderer     latest              ec4ca236d3d4        2 months ago        173MB
   hyperledger/fabric-peer        1.4.1               a1e3874f338b        2 months ago        178MB
   hyperledger/fabric-peer        latest              a1e3874f338b        2 months ago        178MB
   hyperledger/fabric-javaenv     1.4.1               b8c9d7ff6243        2 months ago        1.74GB
   hyperledger/fabric-javaenv     latest              b8c9d7ff6243        2 months ago        1.74GB
   hyperledger/fabric-zookeeper   0.4.15              20c6045930c8        3 months ago        1.43GB
   hyperledger/fabric-zookeeper   latest              20c6045930c8        3 months ago        1.43GB
   hyperledger/fabric-kafka       0.4.15              b4ab82bbaf2f        3 months ago        1.44GB
   hyperledger/fabric-kafka       latest              b4ab82bbaf2f        3 months ago        1.44GB
   hyperledger/fabric-couchdb     0.4.15              8de128a55539        3 months ago        1.5GB
   hyperledger/fabric-couchdb     latest              8de128a55539        3 months ago        1.5GB
   ```

   

