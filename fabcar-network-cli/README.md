# Build Fabcar Network with CLI

* `./byfn.sh generate`
* `./byfn.sh up`
* `./byfn.sh down`

## Tips

1. Using Java Chaincode ../fabcar-chaincode
2. ${COMPOSE_PROJECT_NAME} parameter has a problem, I replaced with hard code one

```
- CORE_VM_DOCKER_HOSTCONFIG_NETWORKMODE=${COMPOSE_PROJECT_NAME}_byfn
```

```
- CORE_VM_DOCKER_HOSTCONFIG_NETWORKMODE=fabcar-network-cli_byfn
```

