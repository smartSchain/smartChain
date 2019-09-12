# 节点搭建详细教程

## 环境准备
- 具有公网IP的linux服务器，且对外打开服务端口
   + [购买阿里云服务器](https://yq.aliyun.com/articles/573919)
   + [购买亚马逊云服务器](https://aws.amazon.com/cn/getting-started/tutorials/launch-a-virtual-machine/?trk=gs_card)

## 节点搭建
- 登录服务器
- 获取节点
```
 wget https://github.com/smartSchain/smartChain/releases/download/2.0/witness_node.zip
```
- 获取后解压节点包
```
 unzip witness_node.zip
```
- 启动节点
```
 nohup ./witness_node -port 12008 -dir /var/dbx/dbxchain -hport 38090 &
```
   + -port 为tcp端口 不写默认12008
   + -dir 为文件存储路径 不写默认/var/dbx/dbxchain
   + -hport 为节点开启http端口号 不写默认38090
