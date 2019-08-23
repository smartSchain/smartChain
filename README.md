# CHAIN DOC

## 节点部署
[节点地址](https://github.com/smartSchain/smartChain/releases/tag/2.0)
- 下载节点，上传自己linux服务器，解压并启动
- 启动命令示例 ./witness_node -port 12008 -dir /var/dbx/dbxchain -hport 38090
- -port 为tcp端口 不写默认12008
- -dir 为文件存储路径 不写默认/var/dbx/dbxchain
- -hport 为节点开启http端口号 不写默认38090

## 超级节点绑定
- 下载2.0节点部署，确保机器有公网ip，且服务端口对公网开放，调用以下命令检查服务健康，返回success则节点服务正常
```curl
	curl -i http://ip:port/health
```
- 绑定节点方法，输入公网ip、超级节点名称、钱包名
```curl
	curl -X POST \
	  http://ip:port/bind_user \
	  -H 'Content-Type: application/json' \
	  -H 'cache-control: no-cache' \
	  -d '{
		"ip": "xx.xx.xx.xxx",
		"nodeName": "nodename",
		"walletName": "walletname"
	}'
```

## 交易所对接
[交易所对接文档](dbx-chain-server.md)

## 区块链浏览器
[区块链浏览器地址](https://adcscan.adc.life)
