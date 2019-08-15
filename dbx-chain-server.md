# dbx-chain-server
> dbx 工具服务(nodejs)
- [github地址](https://github.com/hukaibaihu/dbx-chain-server)

## 运行环境



  - [nodejs](httpss://nodejs.org)
    + npm (nodejs内置)
  - [yarn](httpss://yarnpkg.com)
  - [pm2](httpss://www.npmjs.com/package/pm2)

> `npm`和`yarn`选其一即可，推荐使用`yarn`

## 部署方法

### yarn安装 (推荐)
```bash
# 安装依赖
yarn

# 安装pm2
yarn global add pm2
```
### npm安装
```bash
# 安装依赖
npm install

# 安装pm2
npm install -g pm2
```

### 启动服务(`yarn`|`npm`)
```bash
# 默认端口3000
yarn run prd

# 指定端口(Mac|Linux)
PORT=10080 yarn run prd

# 指定端口(Windows)
set PORT=10080
yarn run prd
```

## URL规则说明

POST: `https://ip:port/{method}`

`{method}`: [dbx-chain-tools](httpss://www.npmjs.com/package/dbx-chain-tools) 工具的方法，参数在请求body中携带

### *生成助记词*

```js
* @return {String} brainKey 16个助记词
```
发起请求:
```curl
curl -H "Content-Type:application/json" -X POST --data '{}' http://localhost:3000/generatorBrainKey
```

```js
$.ajax({
	url: 'http://localhost:3000/generatorBrainKey',
	type: 'POST',
	dataType: 'json',
	contentType: 'application/json',
	data: {},
	success: (res) {
		// 成功
		if (res.code === 200) {
			console.log(res.data)
		} else {
			// 失败
			console.error(res.message)
		}
	}
})
```
正确返回:
```json
{
    "data": "evener scutter patte limmer outcrow bedway tarocco skelpin jarry bilsh seedbed veery peonage adze crested retene",
    "code": 200,
    "message": "Success"
}
```

### *根据助记词或密码生成公私钥对*

```js
* @param  {String} seed 必传。助记词或密码
*
* @return {Object} {privateKey: 私钥, publicKey: 公钥, address: 钱包地址}
```

发起请求:
```curl
curl -H "Content-Type:application/json" -X POST --data '{"seed": "evener scutter patte limmer outcrow bedway tarocco skelpin jarry bilsh seedbed veery peonage adze crested retene"}' http://localhost:3000/generatorKeyFromSeed
```

```js
$.ajax({
	url: 'http://localhost:3000/generatorKeyFromSeed',
	type: 'POST',
	dataType: 'json',
	contentType: 'application/json',
	data: {"seed": "evener scutter patte limmer outcrow bedway tarocco skelpin jarry bilsh seedbed veery peonage adze crested retene"},
	success: (res) {
		// 成功
		if (res.code === 200) {
			console.log(res.data)
		} else {
			// 失败
			console.error(res.message)
		}
	}
})
```

正确返回:
```json
{
    "data": {
        "privateKey": "5K2ziuUN6WoHWrSwYTW8mqNr6oUbTsJbfRmwBWpvDzbPmjUau2N",
        "address": "DBX59oExonpcr21hfcJb15xLYF2a2b2V6wej", 
        "publicKey": "DBX7CQG4Eb7mtQ1oG6r9MT2KKpBatbRdsorJLm76Xy9QA1aV9bgbA"
    },
    "code": 200,
    "message": "Success"
}
```

### *通过私钥生成公私钥对*

```js
* @param  {String} privKey 输入的私钥，必传
*
* @return {Object} {privateKey: 私钥, publicKey: 公钥, address: 钱包地址}
```

发起请求:
```curl
curl -H "Content-Type:application/json" -X POST --data '{"privKey":"5KgUHjz4m6TwDWqFxQZTriN2KQkiyyFv6SjvMbtjp1nGDhMSMbp"}' http://localhost:3000/generatorKeyByPrivKey
```

```js
$.ajax({
	url: 'http://localhost:3000/generatorKeyByPrivKey',
	type: 'POST',
	dataType: 'json',
	contentType: 'application/json',
	data: {
"privKey":"5KgUHjz4m6TwDWqFxQZTriN2KQkiyyFv6SjvMbtjp1nGDhMSMbp" 
},
	success: (res) {
		// 成功
		if (res.code === 200) {
			console.log(res.data)
		} else {
			// 失败
			console.error(res.message)
		}
	}
})
```
正确返回:
```json
{
    "data": {
        "privateKey": "5KgUHjz4m6TwDWqFxQZTriN2KQkiyyFv6SjvMbtjp1nGDhMSMbp",
        "address": "DBXHXMPVhR6X8mtYfT1z8Yz9cS7o6DdpHHJM",
        "publicKey": "DBX8KkbAhost5Hz78q7dimcmoCNX2GHsc36HFmquxFVbpS7wdZooS"
    },
    "code": 200,
    "message": "Success"
}
```

### *交易数据签名*

```json
   * @param  {String} privKey 当前账户的私钥
   * @param  {Object} from 源账户 { id: 账户ID，必传, memoKey: memo公钥 }
   * @param  {Object} to 目标账户 { id: 账户ID，必传, memoKey: memo公钥 }
   * @param  {Object} fee 手续费 { amount: 金额, assetId: 资产ID（币种）}
   * @param  {Object} amount 交易金额 { amount: 金额, assetId: 资产ID（币种）}
   * @param  {String} memo 备注信息
   * @param  {Object} blockHeader 交易块的块头信息 { time, head_block_number, head_block_id }
   * @param  {String} chainId 链ID
   *
   * @return {String} 签名后的JSON串
```
发起请求:
```curl
curl -H "Content-Type:application/json" -X POST --data '{
	"privKey": "5Kj5MVVYWYgB6AhTXZ6KneEUSK43QvWAiLBKwZ6Dr8fYMTU5aBP",
	"from": {
		"id": "1.2.208",
		"memoKey": "DBX8hY6cCBexRbrC9F7U94QJDzazoFJAEqqX9tDisMj6uD4nW8CeC"
	},
	"to": {
		"id": "1.2.207",
		"memoKey": "DBX5Ut7prG8QJeP1t3M7fP9i3XmmMhyCp3qXHkxxfqvTV8fQLrS8S"
	},
	"fee":  {
		"amount": "10",
		"assetId": "1.3.10"
	},
	"amount": {
		"amount": "10000",
		"assetId": "1.3.10"
	},
	"memo": "aaa",
	"blockHeader": {
		"time": "2019-06-20T09:00:00",
		"head_block_number": "21270",
		"head_block_id": "8b6ed59e0e74a84230108ff106087669"
	},
	"chainId": "6e340b9cffb37a989ca544e6bb780a2c78901d3fb33738768511a30617afa01d"
}' http://localhost:3000/buildTransaction
```

```js
$.ajax({
	url: 'http://localhost:3000/buildTransaction',
	type: 'POST',
	dataType: 'json',
	contentType: 'application/json',
	data: {
	"privKey": "5Kj5MVVYWYgB6AhTXZ6KneEUSK43QvWAiLBKwZ6Dr8fYMTU5aBP",
	"from": {
		"id": "1.2.208",
		"memoKey": "DBX8hY6cCBexRbrC9F7U94QJDzazoFJAEqqX9tDisMj6uD4nW8CeC"
	},
	"to": {
		"id": "1.2.207",
		"memoKey": "DBX5Ut7prG8QJeP1t3M7fP9i3XmmMhyCp3qXHkxxfqvTV8fQLrS8S"
	},
	"fee":  {
		"amount": "10",
		"assetId": "1.3.10"
	},
	"amount": {
		"amount": "10000",
		"assetId": "1.3.10"
	},
	"memo": "aaa",
	"blockHeader": {
		"time": "2019-06-20T09:00:00",
		"head_block_number": "21270",
		"head_block_id": "8b6ed59e0e74a84230108ff106087669"
	},
	"chainId": "6e340b9cffb37a989ca544e6bb780a2c78901d3fb33738768511a30617afa01d"
},
	success: (res) {
		// 成功
		if (res.code === 200) {
			console.log(res.data)
		} else {
			// 失败
			console.error(res.message)
		}
	}
})
```
正确返回:
```json
{
    "data": {
        "ref_block_num": 21270,
        "ref_block_prefix": 1118336014,
        "expiration": "2019-06-20T09:00:15",
        "operations": [
            [
                0,
                {
                    "fee": {
                        "amount": "10",
                        "asset_id": "1.3.10"
                    },
                    "from": "1.2.208",
                    "to": "1.2.207",
                    "amount": {
                        "amount": "10000",
                        "asset_id": "1.3.10"
                    },
                    "memo": {
                        "from": "DBX8hY6cCBexRbrC9F7U94QJDzazoFJAEqqX9tDisMj6uD4nW8CeC",
                        "to": "DBX5Ut7prG8QJeP1t3M7fP9i3XmmMhyCp3qXHkxxfqvTV8fQLrS8S",
                        "nonce": "399622255427572",
                        "message": "bb4d6385b75c72ba87b1f64f5f67a4e8"
                    },
                    "extensions": []
                }
            ]
        ],
        "extensions": [],
        "signatures": [
            "201b058b12f78005a82a861b88f7d90842e67c0b2854c6f4445b101b2616261f3339128e4d6283180cafb2595a6ca8fe4396121ac4de8b4a20be9fa3113bad8ae2"
        ]
    },
    "code": 200,
    "message": "Success"
}
```

# 公链API

### 1 *创建账户*

发起请求：

```curl
curl -H "Content-Type:application/json" -X POST --data '{"name":"test-user0",
"owner_key":"DBX7itLPxcJdbSojxkbccJazW3HvQ7wSmnhyC5Us7q6VFZVGyJXPn",
"active_key":"DBX7itLPxcJdbSojxkbccJazW3HvQ7wSmnhyC5Us7q6VFZVGyJXPn",
"memo_key":"DBX7itLPxcJdbSojxkbccJazW3HvQ7wSmnhyC5Us7q6VFZVGyJXPn",
"refcode":null,
"referrer":null	}' https://wallet.adc.life/smartwallet/add_wallet_account
```

请求参数:

```json
{
"name":"test-user0",
"owner_key":"DBX7itLPxcJdbSojxkbccJazW3HvQ7wSmnhyC5Us7q6VFZVGyJXPn",      //所有者账户公钥
"active_key":"DBX7itLPxcJdbSojxkbccJazW3HvQ7wSmnhyC5Us7q6VFZVGyJXPn",     //所有者活动使用的公钥
"memo_key":"DBX7itLPxcJdbSojxkbccJazW3HvQ7wSmnhyC5Us7q6VFZVGyJXPn",       //备忘录相关公钥
"refcode":null,       //父账户的id
"referrer":null	
}
```

正确返回:

```json
{
    "code": 200,
    "data": {
        "owner_key": "DBX7itLPxsJdbSojxkbccJazW3HvQ7wSmnhyC5Us7q6VFZVGyJXPn",
        "name": "test-user2",
        "id": "1.2.250",
        "active_key": "DBX7itLPxsJdbSojxkbccJazW3HvQ7wSmnhyC5Us7q6VFZVGyJXPn"
    },
    "msg": null
}
```

失败的返回，失败返回情况较多，这只是其中一种。格式可能不统一。

```json
{
    "code": 599,
    "data": null,
    "msg": "the ownerKey has been exists"
}
```

### 2 *获取信息*

获取账户余额直接连接服务器节点。具体格式如下：

```curl
curl -H "Content-Type:application/json" -X POST --data '{json内容}'  https://wallet.adc.life/smartwallet/rpc
```

服务端的api是有些是存在限制的，需要登陆后请求服务后才能放开调用权限。

#### **2.1** *获取账户信息*

发起请求:

```curl
curl -H "Content-Type:application/json" -X POST --data '{ "id": "1",
    "method": "call",
    "params": [
        0,                     
        "get_account_by_name",
        [
            "fafafa"
        ]
    ]}'  https://wallet.adc.life/smartwallet/rpc
```

请求参数:

```json
{
    "id": "1",
    "method": "call",
    "params": [
        0,                     //协议族id固定为0，已经开放
        "get_account_by_name",
        [
            "fafafa"
        ]
    ]
}
```

正确返回:

```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":{\"id\":\"1.2.237\",\"membership_expiration_date\":\"1970-01-01T00:00:00\",\"registrar\":\"1.2.17\",\"referrer\":\"1.2.17\",\"lifetime_referrer\":\"1.2.17\",\"network_fee_percentage\":\"2000\",\"lifetime_referrer_fee_percentage\":3000,\"referrer_rewards_percentage\":0,\"name\":\"curry3\",\"owner\":{\"weight_threshold\":1,\"account_auths\":[],\"key_auths\":[[\"DBX7itLPxcJdbSojfkbccJazW3HvQ7wSmnhyC5Us7q6VFZVGyJXPn\",1]],\"address_auths\":[]},\"active\":{\"weight_threshold\":1,\"account_auths\":[],\"key_auths\":[[\"DBX7itLPxcJdbSojfkbccJazW3HvQ7wSmnhyC5Us7q6VFZVGyJXPn\",1]],\"address_auths\":[]},\"options\":{\"memo_key\":\"DBX7itLPxcJdbSojfkbccJazW3HvQ7wSmnhyC5Us7q6VFZVGyJXPn\",\"voting_account\":\"1.2.5\",\"num_committee\":0,\"votes\":[],\"extensions\":[]},\"statistics\":\"2.6.18\",\"whitelisting_accounts\":[],\"blacklisting_accounts\":[],\"whitelisted_accounts\":[],\"blacklisted_accounts\":[],\"owner_special_authority\":[0,{}],\"active_special_authority\":[0,{}],\"top_n_control_flags\":0}}",
    "msg": null
}
```

失败的返回:

```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"error\":{\"base\":[\"账户不存在\"]}}",
    "msg": null
}
```

#### **2.2** *判断账户是否存在*

发起请求:

```curl
curl -H "Content-Type:application/json" -X POST --data '{ "method": "call",
    "params": [
        0,                   
        "get_full_accounts",
        [
            [
                "fafafaf"
            ],
            true
        ]
    ],
    "id": 28}'  https://wallet.adc.life/smartwallet/rpc
```
请求参数:
```json
{
    "method": "call",
    "params": [
        0,                   //协议族id固定为0，已经开放
        "get_full_accounts",
        [
            [
                "fafafaf"
            ],
            true
        ]
    ],
    "id": 28
}
```

正确返回:

```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":[[\"curry3\",{\"vesting_balances\":[],\"limit_orders\":[],\"withdraws\":[],\"settle_orders\":[],\"registrar_name\":\"curry3\",\"balances\":[{\"owner\":\"1.2.237\",\"balance\":0.0,\"asset_type\":\"1.3.10\"}],\"assets\":[],\"call_orders\":[],\"referrer_name\":\"curry3\",\"lifetime_referrer_name\":\"curry3\",\"votes\":[],\"proposals\":[],\"account\":{\"owner\":{\"address_auths\":[],\"key_auths\":[[\"DBX7itLPxcJdbSojfkbccJazW3HvQ7wSmnhyC5Us7q6VFZVGyJXPn\",1]],\"weight_threshold\":1,\"account_auths\":[]},\"whitelisted_accounts\":[],\"registrar\":\"1.2.17\",\"owner_special_authority\":[0,{}],\"active_special_authority\":[0,{}],\"lifetime_referrer\":\"1.2.17\",\"lifetime_referrer_fee_percentage\":3000,\"referrer_rewards_percentage\":0,\"blacklisting_accounts\":[],\"active\":{\"address_auths\":[],\"key_auths\":[[\"DBX7itLPxcJdbSojfkbccJazW3HvQ7wSmnhyC5Us7q6VFZVGyJXPn\",1]],\"weight_threshold\":1,\"account_auths\":[]},\"top_n_control_flags\":0,\"network_fee_percentage\":\"2000\",\"referrer\":\"1.2.17\",\"membership_expiration_date\":\"1970-01-01T00:00:00\",\"name\":\"curry3\",\"options\":{\"extensions\":[],\"memo_key\":\"DBX7itLPxcJdbSojfkbccJazW3HvQ7wSmnhyC5Us7q6VFZVGyJXPn\",\"voting_account\":\"1.2.5\",\"num_committee\":0,\"votes\":[]},\"id\":\"1.2.237\",\"blacklisted_accounts\":[],\"whitelisting_accounts\":[],\"statistics\":\"2.6.18\"},\"statistics\":{\"owner\":\"1.2.18\",\"pending_fees\":0,\"lifetime_fees_paid\":4000000,\"pending_vested_fees\":0,\"id\":\"1.2.237\",\"total_ops\":5,\"total_core_in_orders\":0,\"most_recent_op\":\"2.9.28\",\"removed_ops\":0}}]]}",
    "msg": null
}
```

失败的返回:

```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"error\":{\"base\":[\"账户不存在\"]}}",
    "msg": null
}
```

#### **2.3** *获取链信息*

获取链信息需要调用服务端的database_api接口。

database_api默认是打开的，并且不需要用户登录即可直接调用。

##### *2.3.1* *获取链ID*

发起请求:

```curl
curl -H "Content-Type:application/json" -X POST --data '{ "method":"call","params":[0,"get_chain_id",[]],"id":1}'  https://wallet.adc.life/smartwallet/rpc
```
请求参数:
```json
{"method":"call","params":[0,"get_chain_id",[]],"id":1}
```
正确返回:
```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":\"6e340b9cffb37a989ca544e6bb780a2c78901d3fb33738768511a30617afa01d\"}",
    "msg": null
}
```

##### 2.3.2 *通过名称获取余额*

发起请求:

```curl
curl -H "Content-Type:application/json" -X POST --data '{ "id": 1,             
    "method": "call",
    "params": [
        0,               
        "get_named_account_balances",     
        [
            "root",          
            [
                "1.3.10"  
            ]
        ]
    ]}'  https://wallet.adc.life/smartwallet/rpc
```
请求参数:
```json
{
    "id": 1,                   //RPC自增ID
    "method": "call",
    "params": [
        0,               //系统默认打开的database_api接口ID
        "get_named_account_balances",     //接口名称
        [
            "root",          //账户名称
            [
                "1.3.10"   //请求的币种列表，1.3.10代表ADC
            ]
        ]
    ]
}
```

正确返回:

```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":[{\"amount\":31267964134.46,\"asset_id\":\"1.3.10\"}]}",
    "msg": null
}
```

失败返回，失败返回情况较多，格式不统一。

##### *2.3.3* *通过账户id获取余额*

发起请求:
```curl
curl -H "Content-Type:application/json" -X POST --data '{ "id": 1,
    "method": "call",
    "params": [
        0,
        "get_account_balances",
        [
            "1.2.208",
            [
                "1.3.10"
            ]
        ]
    ]}'  https://wallet.adc.life/smartwallet/rpc
```
请求参数:
```json
{
    "id": 1,
    "method": "call",
    "params": [
        0,
        "get_account_balances",
        [
            "1.2.208",   //钱包ID
            [
                "1.3.10" //币种ID
            ]
        ]
    ]
}
```

正确返回:

```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":[{\"amount\":995011642720122.00,\"asset_id\":\"1.3.10\"}]}",
    "msg": null
}
```

失败返回，失败返回情况较多，格式不统一。

##### *2.3.4* *获取块信息*

发起请求:

```curl
curl -H "Content-Type:application/json" -X POST --data '{
    "method": "call",
    "params": [
        0,
        "get_block",
        [
            100
        ]
    ],
    "id": 1
}'  https://wallet.adc.life/smartwallet/rpc
```
请求参数:
```json
{
    "method": "call",
    "params": [
        0,
        "get_block",
        [
            100
        ]
    ],
    "id": 1
}
```

正确返回:

```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":{\"previous\":\"0030777fec5c2b20d2442be48adcb2bde7bbc297\",\"timestamp\":\"2019-06-22 05:27:33\",\"witness\":\"1.6.5\",\"transaction_merkle_root\":\"0000000000000000000000000000000000000000\",\"extensions\":[],\"witness_signature\":\"205a776cea3ebe94455f253803ab7ce27a64de7fb9f6bd1b5d0d377b44697738445ef19b950b3c9beb50752010e0ada3c2d37c978eab01b762d4276a70f9982d89\",\"transactions\":[]}}",
    "msg": null
}
```
##### *2.3.5* *get_account_history_by_operations*

发起请求:

```curl
curl -H "Content-Type:application/json" -X POST --data '{
    "id": 1,
    "method": "call",
    "params": [
        "history",
        "get_account_history_by_operations",
        [
            "id",
            [
                0
            ],
            1,
            10
        ]
    ]
}'  https://wallet.adc.life/smartwallet/rpc
```
请求参数:
```json
{
    "id": 1,
    "method": "call",
    "params": [
        "history",
        "get_account_history_by_operations",
        [
            "id",
            [
                0
            ],
            1,     //offset
            10     //limit
        ]
    ]
}
```

正确返回:

```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":{\"operation_history_objs\":[],\"total_count\":1}}",
    "msg": null
}
```

##### *2.3.6* *get_account_history_size*

发起请求:

```curl
curl -H "Content-Type:application/json" -X POST --data '{
    "id": 1,
    "method": "call",
    "params": [
        "history",
        "get_account_history_size",
        [
            "1.2.1944"
        ]
    ]
}'  https://wallet.adc.life/smartwallet/rpc
```
请求参数:
```json
{
    "id": 1,
    "method": "call",
    "params": [
        "history",
        "get_account_history_size",
        [
            "1.2.1944"            //accountID
        ]
    ]
}
```

正确返回:

```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":15}",
    "msg": null
}
```

#### **2.4** *转账*

转账操作需要用户登录本地钱包，即程序需要拿到私钥。

转账操作需要调用服务端的database_api家族接口。

##### *2.4.1* *获取ChainId*

发起请求:
```curl
curl -H "Content-Type:application/json" -X POST --data '{"method":"call","params":[0,"get_chain_id",[]],"id":1}'  https://wallet.adc.life/smartwallet/rpc
```

请求参数:
```json
{
  "method":"call",
  "params":[0,"get_chain_id",[]],
  "id":1
}
```

正确返回:

```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":\"6e340b9cffb37a989ca544e6bb780a2c78901d3fb33738768511a30617afa01d\"}",
    "msg": null
}
```

##### 2.4.2*获取blockHeader*

即获取交易发起者的owner公钥和active公钥。

发起请求:

```curl
curl -H "Content-Type:application/json" -X POST --data '{
    "method": "call",
    "params": [
        0,
        "get_objects",
        [
            ["2.1.0"]
        ]
    ],
    "id": 1
}'  https://wallet.adc.life/smartwallet/rpc
```
请求参数:
```json
{
    "method": "call",
    "params": [
        0,
        "get_objects",
        [
            ["2.1.0"]  //2.1.xxx代表获取块信息,1.3.xxx代表获取币种信息,1.11.xxx代表获取交易信息,其它代表获取用户信息
        ]
    ],
    "id": 1
}
```

正确返回:

```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":[{\"id\":\"2.1.0\",\"head_block_number\":5759975,\"head_block_id\":\"bdbc6640ee26b252a2770c0600efdf03\",\"time\":\"2019-06-21T12:23:31\",\"next_maintenance_time\":\"2019-06-21T12:23:31\",\"last_budget_time\":\"2019-06-21T12:23:31\",\"witness_budget\":89000000,\"accounts_registered_this_interval\":0,\"recently_missed_count\":459932,\"current_aslot\":3491147,\"recent_slots_filled\":\"bdbc6640ee26b252a2770c0600efdf03\",\"dynamic_flags\":0,\"last_irreversible_block_num\":5759975}]}",
    "msg": null
}
```

请求参数:
```json
{
    "method": "call",
    "params": [
        0,
        "get_objects",
        [
            ["1.11.4791670"]  //2.1.xxx代表获取块信息,1.3.xxx代表获取币种信息,1.11.xxx代表获取交易信息,其它代表获取用户信息
        ]
    ],
    "id": 1
}
```

正确返回:

```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":[{\"result\":[0,{}],\"block_num\":5811551,\"op\":[0,{\"amount\":{\"amount\":2000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"id\":\"1.11.4791670\",\"hash\":\"8b513a5a568718c2ed862e6bed4ff2bd\"}]}",
    "msg": null
}
```

请求参数:
```json
{
    "method": "call",
    "params": [
        0,
        "get_objects",
        [
            ["1.3.10"]  //2.1.xxx代表获取块信息,1.3.xxx代表获取币种信息,1.11.xxx代表获取交易信息,其它代表获取用户信息
        ]
    ],
    "id": 1
}
```

正确返回:

```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":[{\"symbol\":\"ADC\",\"precision\":6,\"id\":\"1.3.10\"}]}",
    "msg": null
}
```

请求参数:
```json
{
    "method": "call",
    "params": [
        0,
        "get_objects",
        [
            ["1.2.405"]  //2.1.xxx代表获取块信息,1.3.xxx代表获取币种信息,1.11.xxx代表获取交易信息,其它代表获取用户信息
        ]
    ],
    "id": 1
}
```

正确返回:

```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":[{\"id\":\"1.2.405\",\"membership_expiration_date\":\"1970-01-01T00:00:00\",\"registrar\":\"1.2.17\",\"referrer\":\"1.2.17\",\"lifetime_referrer\":\"1.2.17\",\"network_fee_percentage\":\"2000\",\"lifetime_referrer_fee_percentage\":3000,\"referrer_rewards_percentage\":0,\"name\":\"walh1314\",\"owner\":{\"weight_threshold\":1,\"account_auths\":[],\"key_auths\":[[\"DBX8KZzyaoJ8oTabuT7pcXqyT783u1rmU9ZduU62eXQnVRJdTMuxh\",1]],\"address_auths\":[]},\"active\":{\"weight_threshold\":1,\"account_auths\":[],\"key_auths\":[[\"DBX8KZzyaoJ8oTabuT7pcXqyT783u1rmU9ZduU62eXQnVRJdTMuxh\",1]],\"address_auths\":[]},\"options\":{\"memo_key\":\"DBX8KZzyaoJ8oTabuT7pcXqyT783u1rmU9ZduU62eXQnVRJdTMuxh\",\"voting_account\":\"1.2.5\",\"num_committee\":0,\"votes\":[],\"extensions\":[]},\"statistics\":\"2.6.18\",\"whitelisting_accounts\":[],\"blacklisting_accounts\":[],\"whitelisted_accounts\":[],\"blacklisted_accounts\":[],\"owner_special_authority\":[0,{}],\"active_special_authority\":[0,{}],\"top_n_control_flags\":0}]}",
    "msg": null
}
```

##### *2.4.3* *buildTransaction*

拿2.4.2接口返回的head_block_number、head_block_id、time三个值

费用值为千分之一，别超过1个ADC即可

发起请求:

```curl
curl -H "Content-Type:application/json" -X POST --data '{
	"privKey": "5Kj5MVVYWYgB6AhTXZ6KneEUSK43QvWAiLBKwZ6Dr8fYMTU5aBP",
	"from": {
		"id": "1.2.208",
		"memoKey": "DBX8hY6cCBexRbrC9F7U94QJDzazoFJAEqqX9tDisMj6uD4nW8CeC"
	},
	"to": {
		"id": "1.2.207",
		"memoKey": "DBX5Ut7prG8QJeP1t3M7fP9i3XmmMhyCp3qXHkxxfqvTV8fQLrS8S"
	},
	"fee":  {
		"amount": "10",
		"assetId": "1.3.10"
	},
	"amount": {
		"amount": "10000",
		"assetId": "1.3.10"
	},
	"memo": "aaa",
	"blockHeader": {
		"time": "2019-06-20T09:00:00",
		"head_block_number": "21270",
		"head_block_id": "8b6ed59e0e74a84230108ff106087669"
	},
	"chainId": "6e340b9cffb37a989ca544e6bb780a2c78901d3fb33738768511a30617afa01d"
}'  http://localhost:3000/buildTransaction
```

请求参数:
```json
{
	"privKey": "5Kj5MVVYWYgB6AhTXZ6KneEUSK43QvWAiLBKwZ6Dr8fYMTU5aBP",
	"from": {
		"id": "1.2.208",
		"memoKey": "DBX8hY6cCBexRbrC9F7U94QJDzazoFJAEqqX9tDisMj6uD4nW8CeC"
	},
	"to": {
		"id": "1.2.207",
		"memoKey": "DBX5Ut7prG8QJeP1t3M7fP9i3XmmMhyCp3qXHkxxfqvTV8fQLrS8S"
	},
	"fee":  {
		"amount": "10",
		"assetId": "1.3.10"
	},
	"amount": {
		"amount": "10000",
		"assetId": "1.3.10"
	},
	"memo": "aaa",
	"blockHeader": {
		"time": "2019-06-20T09:00:00",
		"head_block_number": "21270",
		"head_block_id": "8b6ed59e0e74a84230108ff106087669"
	},
	"chainId": "6e340b9cffb37a989ca544e6bb780a2c78901d3fb33738768511a30617afa01d"
}
```

正确返回:

```json
{
  "data": {
    "ref_block_num": 21270,
    "ref_block_prefix": 1118336014,
    "expiration": "2019-06-20T09:00:15",
    "operations": [
      [
        0,
        {
          "fee": {
            "amount": "10",
            "asset_id": "1.3.10"
          },
          "from": "1.2.208",
          "to": "1.2.207",
          "amount": {
            "amount": "10000",
            "asset_id": "1.3.10"
          },
          "memo": {
            "from": "DBX8hY6cCBexRbrC9F7U94QJDzazoFJAEqqX9tDisMj6uD4nW8CeC",
            "to": "DBX5Ut7prG8QJeP1t3M7fP9i3XmmMhyCp3qXHkxxfqvTV8fQLrS8S",
            "nonce": "399663000115450",
            "message": "8e9fb29af5dd17da5643db089326826d"
          },
          "extensions": []
        }
      ]
    ],
    "extensions": [],
    "signatures": [
      "201f47ecefb274e28e1f533f751fac534f6e6c83e32bda807e0857a55355a97f20639ad6f9ab1c12a49619516d50425177466ac749a186f290c9bb05e4ca8eab13"
    ]
  },
  "code": 200,
  "message": "Success"
}
```

##### *2.4.4* *broadcast_transaction_with_callback*
实际发起交易的地方

发起请求:

```curl
curl -H "Content-Type:application/json" -X POST --data '{
    "method": "call",
    "params": [
        2,
        "broadcast_transaction_with_callback",
        [
            510,
            {
                "ref_block_num": 21270,
                "ref_block_prefix": 1118336014,
                "expiration": "2019-06-20T09:00:15",
                "operations": [
                    [
                        0,
                        {
                            "fee": {
                                "amount": "10",
                                "asset_id": "1.3.10"
                            },
                            "from": "1.2.208",
                            "to": "1.2.207",
                            "amount": {
                                "amount": "10000",
                                "asset_id": "1.3.10"
                            },
                            "memo": {
                                "from": "DBX8hY6cCBexRbrC9F7U94QJDzazoFJAEqqX9tDisMj6uD4nW8CeC",
                                "to": "DBX5Ut7prG8QJeP1t3M7fP9i3XmmMhyCp3qXHkxxfqvTV8fQLrS8S",
                                "nonce": "399644261061880",
                                "message": "2a568e9c522c7d1635b7657a155aee28"
                            },
                            "extensions": []
                        }
                    ]
                ],
                "extensions": [],
                "signatures": [
                    "1f3c9b7de077edc8b4a116e757f84438cdc33a61ef01af0260e7c24b855f3407f0722c04fb9ea8b0ead870347a88f1b622e3bec4a6d2f3a5b38838488518eddf5e"
                ]
            }
        ]
    ],
    "id": 510
}'   https://wallet.adc.life/smartwallet/rpc
```
请求参数:
```json
{
    "method": "call",
    "params": [
        2,
        "broadcast_transaction_with_callback",
        [
            510,
            {
                "ref_block_num": 21270,
                "ref_block_prefix": 1118336014,
                "expiration": "2019-06-20T09:00:15",
                "operations": [
                    [
                        0,
                        {
                            "fee": {
                                "amount": "10",
                                "asset_id": "1.3.10"
                            },
                            "from": "1.2.208",
                            "to": "1.2.207",
                            "amount": {
                                "amount": "10000",
                                "asset_id": "1.3.10"
                            },
                            "memo": {
                                "from": "DBX8hY6cCBexRbrC9F7U94QJDzazoFJAEqqX9tDisMj6uD4nW8CeC",
                                "to": "DBX5Ut7prG8QJeP1t3M7fP9i3XmmMhyCp3qXHkxxfqvTV8fQLrS8S",
                                "nonce": "399644261061880",
                                "message": "2a568e9c522c7d1635b7657a155aee28"
                            },
                            "extensions": []
                        }
                    ]
                ],
                "extensions": [],
                "signatures": [
                    "1f3c9b7de077edc8b4a116e757f84438cdc33a61ef01af0260e7c24b855f3407f0722c04fb9ea8b0ead870347a88f1b622e3bec4a6d2f3a5b38838488518eddf5e"
                ]
            }
        ]
    ],
    "id": 510
}
```
正确返回:
```json
{"code":200,"data":"{\"id\":510,\"jsonrpc\":\"2.0\",\"result\":\"1.11.4791596\"}","msg":null}
```


#### **2.5** *转账记录*

转账操作需要调用服务端的history_api家族接口，服务器默认已经打开的，格式如下：

```curl
curl -H "Content-Type:application/json" -X POST --data '{json内容}'  https://wallet.adc.life/smartwallet/rpc
```

##### *2.5.1* *获取历史*

发送请求:

```curl
curl -H "Content-Type:application/json" -X POST --data '{"method": "call",
    "params": [
        3,                          
        "get_account_history",     
        [
            "1.2.231",             
            "1.11.0",              
            10,                    
            "1.11.0"               
        ]
    ],
    "id": 1}'  https://wallet.adc.life/smartwallet/rpc
```

请求参数:
```json
{
    "method": "call",
    "params": [
        3,                          //系统默认打开的history_api接口ID
        "get_account_history",     //获取历史接口
        [
            "1.2.231",             //账户id
            "1.11.0",              //跟账户相关的ID. 这里代表历史ID。这里是开始的历史id。基本写死即可。
            10,                    //获取多少个
            "1.11.0"               //跟账户相关的ID. 这里代表历史ID。这里是结束的历史id。基本写死即可。
        ]
    ],
    "id": 1
}
```

正确返回:

```json
{
    "code": 200,
    "data": "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":[{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"acfaa39e8e101dc39391be106d84db59\",\"nonce\":\"399776462685223\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5861373,\"id\":\"1.11.4810386\",\"hash\":\"9f4a2709556a04f31002dcd99cd3e5ba\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"d49e8077b4ff4ce47cdb2e03374fabb4\",\"nonce\":\"399776454845478\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5861367,\"id\":\"1.11.4810383\",\"hash\":\"554bb81b83f0fa5e2eb5dbd5a2d4d8d5\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"f8cf5a975540a66a0bdb8ca8d068120c\",\"nonce\":\"399776432367397\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5861349,\"id\":\"1.11.4810380\",\"hash\":\"3a43076f4755f5786daba21aac34e20b\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"da1b1fd4cd93699b55399ade0c737112\",\"nonce\":\"399776392178212\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5861318,\"id\":\"1.11.4810370\",\"hash\":\"e7f52f4f7c1ef961e93eabeea79e5cbb\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"b016b3eaac67623bbedc338acec466a0\",\"nonce\":\"399776386410787\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5861314,\"id\":\"1.11.4810369\",\"hash\":\"2258def10fdaf2d852959efbb9cd6425\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"35cdf68fba6d51313cd57c0934b6eccf\",\"nonce\":\"399776342608930\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5861284,\"id\":\"1.11.4810359\",\"hash\":\"069f9c5199d3a174ac2ac6f2c637edd8\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"8bd80cd698fb81e0d8d546e085e316cd\",\"nonce\":\"399776284409376\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5861234,\"id\":\"1.11.4810341\",\"hash\":\"062aa668be8669de7f43ef873d2d7146\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":1000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX4uXp8cC2mCSabtWT3TZncpdDUyM4n4uqUEd2WFYanUDhkhoZ3M\",\"to\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"message\":\"2b65e87b4300b835de0089b934138342c2f672c653ca982ef439cc18102c6b55\",\"nonce\":\"399776226392607\"},\"from\":\"1.2.2923\",\"to\":\"1.2.1944\"}],\"block_num\":5861189,\"id\":\"1.11.4810310\",\"hash\":\"eb022e8632fb579138c10cf958625f0a\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":140000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"to\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"message\":\"01a2f7e71ed7e1a68470cd7cf11d524d44a6b72d5347275f6e3ab24a6feace54\",\"nonce\":\"399775622459678\"},\"from\":\"1.2.2011\",\"to\":\"1.2.1944\"}],\"block_num\":5860717,\"id\":\"1.11.4810099\",\"hash\":\"943f16ecb0eae2ac4a22887de3cbc7e3\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":140000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"to\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"message\":\"ca6d5c76968f47359f3591d6033a2b0faa5ddfe53eb18ea8c986af4257d1e6f2\",\"nonce\":\"399775616623389\"},\"from\":\"1.2.2011\",\"to\":\"1.2.1944\"}],\"block_num\":5860712,\"id\":\"1.11.4810097\",\"hash\":\"cdcb2e337fc5a566d2184586d16f0da5\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":140000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"to\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"message\":\"5fbd9b8271ea88abbeabc99a5722e484c0cc7382ed9996692ada68717e8b7675\",\"nonce\":\"399775569005084\"},\"from\":\"1.2.2011\",\"to\":\"1.2.1944\"}],\"block_num\":5860675,\"id\":\"1.11.4810078\",\"hash\":\"3e923020e4d24e85a4819bb07ce9fc52\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":55000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":5501,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX51YPWUkUyCtTXBAUGQMGr9LGaSbnHP9rpMAVYYGL8hnAKozp26\",\"to\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"message\":\"322bd5aa07efdae81f7f6c5aef37f45d2ae0490fcba4e299b94948b3cba149ce\",\"nonce\":\"399774622038834\"},\"from\":\"1.2.977\",\"to\":\"1.2.1944\"}],\"block_num\":5859937,\"id\":\"1.11.4809855\",\"hash\":\"932a6b22a19badfda60b836228f39e5c\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":100000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100000,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX51YPWUkUyCtTXBAUGQMGr9LGaSbnHP9rpMAVYYGL8hnAKozp26\",\"message\":\"c17cdaa8faf026ca63411cf76800a345\",\"nonce\":\"399774556982043\"},\"from\":\"1.2.1944\",\"to\":\"1.2.977\"}],\"block_num\":5859884,\"id\":\"1.11.4809795\",\"hash\":\"3016e2cdd96dd27268af7f1a575aea6a\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":66000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":6600,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX4zfeURe6Q4gTHP5j5PvC3vEbdWa627hWThV3j1vcrDj9mBepBf\",\"to\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"message\":\"e5bab140748aaacd58cf9c2790e51b044d0b88d431d4057d7f5aa62a6be9c3f1\",\"nonce\":\"399774353704917\"},\"from\":\"1.2.7202\",\"to\":\"1.2.1944\"}],\"block_num\":5859728,\"id\":\"1.11.4809681\",\"hash\":\"60faae070b9dd1be16819314e6db58b1\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":10000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":10000,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX51YPWUkUyCtTXBAUGQMGr9LGaSbnHP9rpMAVYYGL8hnAKozp26\",\"message\":\"b37f57ee27c71b5a5b8138cb95e2b341\",\"nonce\":\"399774217025306\"},\"from\":\"1.2.1944\",\"to\":\"1.2.977\"}],\"block_num\":5859619,\"id\":\"1.11.4809634\",\"hash\":\"f39f11f1a76fde4b7d45e28b004f039a\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":88000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":8800,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX4zfeURe6Q4gTHP5j5PvC3vEbdWa627hWThV3j1vcrDj9mBepBf\",\"to\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"message\":\"82f03f8371a822c3ac9d010bdcc8a5b4a9f3d66af39897c018647c1875786ff4\",\"nonce\":\"399773831184596\"},\"from\":\"1.2.7202\",\"to\":\"1.2.1944\"}],\"block_num\":5859320,\"id\":\"1.11.4809547\",\"hash\":\"682d63667361b6455e306c64b24e2042\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"110b9c0fdf6403a5fd84abd9a6de5bd3\",\"nonce\":\"399754876275481\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5844509,\"id\":\"1.11.4803120\",\"hash\":\"1824fd3257098a9778ee989de520e5cf\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":1,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":0,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"97b967dd026b29533c094270a6d2f300\",\"nonce\":\"399754556962584\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5844259,\"id\":\"1.11.4803011\",\"hash\":\"6ebbdeb248a0af0e1c389c23d7409a01\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":10,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":0,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"2762fa9dadc8d65722122ccd08422af5\",\"nonce\":\"399754532835351\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5844240,\"id\":\"1.11.4803006\",\"hash\":\"83f9c4c357179654ae8980e88eaa3501\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":7000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":7000,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"e17e04b659dfe38d6562b422a19158a4\",\"nonce\":\"399754018546966\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5843839,\"id\":\"1.11.4802761\",\"hash\":\"56966525cf988bf4c886e14f7d08e860\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":88000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":8800,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX4zfeURe6Q4gTHP5j5PvC3vEbdWa627hWThV3j1vcrDj9mBepBf\",\"to\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"message\":\"75d4901017e2a009840cad4ff58ce063afc2479871f268033058d6dca9ccea5e\",\"nonce\":\"399729569478035\"},\"from\":\"1.2.7202\",\"to\":\"1.2.1944\"}],\"block_num\":5824741,\"id\":\"1.11.4796018\",\"hash\":\"cc607f42306cf9a64ff2c22a2d4e92d4\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":100000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":10000,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX4zfeURe6Q4gTHP5j5PvC3vEbdWa627hWThV3j1vcrDj9mBepBf\",\"to\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\"},\"from\":\"1.2.7202\",\"to\":\"1.2.1944\"}],\"block_num\":5824515,\"id\":\"1.11.4795958\",\"hash\":\"9360dfeffd5ca69d7aa93d2e9f5ac906\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":99000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":1000,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"25e5c30a61b65f8c7d4a779f91c20906\",\"nonce\":\"399728643109140\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5824014,\"id\":\"1.11.4795615\",\"hash\":\"b8ce7854bffe3e5362bf82fa58837fc2\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":140000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"to\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"message\":\"104c38ee0ce5011bada5022172d2b081f163444c0d030e698e71ef779232457b\",\"nonce\":\"399713877225980\"},\"from\":\"1.2.2011\",\"to\":\"1.2.1944\"}],\"block_num\":5812478,\"id\":\"1.11.4791672\",\"hash\":\"c69af082647b81eb1ce50ba9f4276cd4\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":2000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"720cb47889001351446da8e5ed251333\",\"nonce\":\"399712690880763\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5811551,\"id\":\"1.11.4791670\",\"hash\":\"8b513a5a568718c2ed862e6bed4ff2bd\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":10000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"171155f6d36000425c61e98553952c91\",\"nonce\":\"399712553257719\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5811444,\"id\":\"1.11.4791669\",\"hash\":\"69a65c3fe74f8ed8b2a8eeb18fc537b2\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":10000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"192fc028f5ab90297ad94fb9e40dd0da\",\"nonce\":\"399712083142383\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5811077,\"id\":\"1.11.4791668\",\"hash\":\"862be3d644c224ff90541458ef73c0bd\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":10000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"e057285be9cf11e3979d355744f2244e\",\"nonce\":\"399704776371438\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5805368,\"id\":\"1.11.4791660\",\"hash\":\"ceb99978d8244842995f5a41c4422e49\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":10000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"314839c54e5da9d4efd029c4cb493d2a\",\"nonce\":\"399704732881901\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5805334,\"id\":\"1.11.4791659\",\"hash\":\"2173a42ff76922c2bc4c64b5ee70cc90\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":30000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":0,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX7QZRF58v8eG6uF56dWSgUSavfKTSHBkC49xN942CA6wrPRoR61\",\"to\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\"},\"from\":\"1.2.3845\",\"to\":\"1.2.1944\"}],\"block_num\":5805334,\"id\":\"1.11.4791658\",\"hash\":\"15ec5686b1c86b48d17feffefd17d9a8\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":400000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"14b3af170bb4e11f792f0bc0f128b2af\",\"nonce\":\"399666603649207\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5775546,\"id\":\"1.11.4791638\",\"hash\":\"294cc002684254ae2ab97f077d702855\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":300000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"8e43e794d1ade5df4c4a82036395518b\",\"nonce\":\"399664713721014\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5774069,\"id\":\"1.11.4791604\",\"hash\":\"996e351d80e8921aeecd68ac8e205b15\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":2000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"6441fc1fb2cc6a92fd73ac576cc5c21b\",\"nonce\":\"399659729794473\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5770175,\"id\":\"1.11.4791588\",\"hash\":\"9ddbff6f3b00b622a83a6c1cdfa8f3d9\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":2000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"f0799db86b7ef90769f89d1fbf79d6fd\",\"nonce\":\"399659701834408\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5770154,\"id\":\"1.11.4791587\",\"hash\":\"871125acf0323f6fd534d49707b6d509\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":2000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"ea3605174c1493d3c14214b5b3b9fdfb\",\"nonce\":\"399659657515175\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5770119,\"id\":\"1.11.4791586\",\"hash\":\"61429d0e97427b538b85609ffb58b3d9\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":2000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"728a665bdc1894086c5bdfdf135c0cad\",\"nonce\":\"399659578633894\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5770057,\"id\":\"1.11.4791585\",\"hash\":\"c4ae9165e107a3b96fb84d48a36101b5\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":20000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"2cb9d17baceae53a1c888d542b4bae82\",\"nonce\":\"399658531242661\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5769239,\"id\":\"1.11.4791578\",\"hash\":\"9f7cbd5383f0549336d0514f3c47a782\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":20000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"b377d182c9bf04ac5db839724a4b3b38\",\"nonce\":\"399647775098488\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5760836,\"id\":\"1.11.4791576\",\"hash\":\"4d39d98f36113c54588f70c79637e226\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":10000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"de1b85066b9116b1d3b0ff9446a7b8d2\",\"nonce\":\"399647657833588\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5760744,\"id\":\"1.11.4791575\",\"hash\":\"3f1aff5f21df4b904e5778f3f4c12e56\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":10000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"2eb47c3cf60c16607b9e758e858d652c\",\"nonce\":\"399647624565619\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5760718,\"id\":\"1.11.4791574\",\"hash\":\"0f0e0c9b57eb0c2350411ceb0fbfa617\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":10000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"109a193c794153f86e07a6628b9fd30b\",\"nonce\":\"399647606471282\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5760704,\"id\":\"1.11.4791573\",\"hash\":\"09c76dccc4aa57896f5739fb9e7b0286\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":10000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"c8d5f55ba95b2eb304b616bb280d21f3\",\"nonce\":\"399647573975665\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5760679,\"id\":\"1.11.4791572\",\"hash\":\"9c50f0fb9784a9d6a173a6fd0b637ea7\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":10000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"403c4f1e7eb00b4a4155b255973fa3b5\",\"nonce\":\"399647517694832\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5760635,\"id\":\"1.11.4791571\",\"hash\":\"fc8e49a3d37aed7d3ef3fa3fa48a1a53\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":1000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"ce016c7f4232303882a0a12bc1646cda\",\"nonce\":\"399647439128431\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5760573,\"id\":\"1.11.4791569\",\"hash\":\"eb560e87b82b9043b293c34bdc13e083\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":1000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"6ad85ce37a531e6cd7fcba6df7d7f355\",\"nonce\":\"399647422442350\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5760560,\"id\":\"1.11.4791568\",\"hash\":\"b4a7c935059b1ab50f2bd6bd3bb16303\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":1000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":100,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"to\":\"DBX6nepCMPhSEp8h1CakmzstMX5orBVP7iU4BM6RyLfvegpJeTbVH\",\"message\":\"4e7b256e50be3b3007ded0e665ba1278\",\"nonce\":\"399647161926505\"},\"from\":\"1.2.1944\",\"to\":\"1.2.2011\"}],\"block_num\":5760357,\"id\":\"1.11.4791565\",\"hash\":\"ab73360750ef6dd8f786163a24f643f0\"},{\"result\":[0,{}],\"op\":[0,{\"amount\":{\"amount\":100000000,\"asset_id\":\"1.3.10\"},\"extensions\":[],\"fee\":{\"amount\":0,\"asset_id\":\"1.3.10\"},\"memo\":{\"from\":\"DBX5qccGEUE8L8MkJfve7U3P8E98N4d9kytM9TccpDtfA57cY8q4d\",\"to\":\"DBX5KuAQ9dpnk1ygkto4T9g4wUUKPXTYPamh9eYqrxPjLGtJ8iNS6\",\"message\":\"5ae32d00acc22bae49f16e864dfb9e91\",\"nonce\":\"397011330405582\"},\"from\":\"1.2.208\",\"to\":\"1.2.1944\"}],\"block_num\":5753283,\"id\":\"1.11.4791557\",\"hash\":\"08f1d66f408c13589d0e6381a7c9b223\"}]}",
    "msg": null
}
```

##### *2.5.2* *解密备注信息*

```js
   * @param  {String} privKey 当前账户的私钥
   * @param  {Object} memo 备注信息
   *
   * @return {String} 解密后的备注
```
发起请求:
```curl
curl -H "Content-Type:application/json" -X POST --data '{
    "privKey": "5Kj5MVVYWYgB6AhTXZ6KneEUSK43QvWAiLBKwZ6Dr8fYMTU5aBP",
    "memo": {
		"from": "DBX8hY6cCBexRbrC9F7U94QJDzazoFJAEqqX9tDisMj6uD4nW8CeC",
        "to": "DBX5Ut7prG8QJeP1t3M7fP9i3XmmMhyCp3qXHkxxfqvTV8fQLrS8S",
        "nonce": "399644261061880",
        "message": "2a568e9c522c7d1635b7657a155aee28"
	}
}' http://localhost:3000/decodeMemo
```

```js
$.ajax({
	url: 'http://localhost:3000/decodeMemo',
	type: 'POST',
	dataType: 'json',
	contentType: 'application/json',
	data: {
    "privKey": "5Kj5MVVYWYgB6AhTXZ6KneEUSK43QvWAiLBKwZ6Dr8fYMTU5aBP",
    "memo": {
		"from": "DBX8hY6cCBexRbrC9F7U94QJDzazoFJAEqqX9tDisMj6uD4nW8CeC",
        "to": "DBX5Ut7prG8QJeP1t3M7fP9i3XmmMhyCp3qXHkxxfqvTV8fQLrS8S",
        "nonce": "399644261061880",
        "message": "2a568e9c522c7d1635b7657a155aee28"
	}
},
	success: (res) {
		// 成功
		if (res.code === 200) {
			console.log(res.data)
		} else {
			// 失败
			console.error(res.message)
		}
	}
})
```
正确返回:
```json
{
    "data": "aaa",
    "code": 200,
    "message": "Success"
}
```
