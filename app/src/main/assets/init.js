const addressHex = "%1$s";
const rpcURL = "%2$s";
const chainID = "%3$s";
function executeCallback(id, error, value) {Tiger.executeCallback(id, error, value)}
function onSignSuccessful(id, value) {Tiger.executeCallback(id, null, value)}
function onSignError(id, error) {Tiger.executeCallback(id, error, null)}
window.Tiger.init(rpcURL, {
	getAccounts: function (cb) {cb(null, [addressHex])},
	processTransaction: function (tx, cb) {
		console.log('signing a transaction', tx)
		const {
			id = 8888
		} = tx
			Tiger.addCallback(id, cb)
			var gasLimit = tx.gasLimit || tx.gas || null;
		var gasPrice = tx.gasPrice || null;
		var data = tx.data || null;
		var nonce = tx.nonce || -1;
		tiger.signTransaction(id, tx.to || null, tx.value, nonce, gasLimit, gasPrice, data);
	},
	signMessage: function (msgParams, cb) {
		const {
			data
		} = msgParams
			const {
			id = 8888
		} = msgParams
			console.log("signing a message", msgParams)
			Tiger.addCallback(id, cb)
			tiger.signMessage(id, data);
	},
	signPersonalMessage: function (msgParams, cb) {
		const {
			data
		} = msgParams
			const {
			id = 8888
		} = msgParams
			console.log("signing a personal message", msgParams)
			Tiger.addCallback(id, cb)
			tiger.signPersonalMessage(id, data);
	},
	signTypedMessage: function (msgParams, cb) {
		const {
			data
		} = msgParams
			const {
			id = 8888
		} = msgParams
			Tiger.addCallback(id, cb)
			tiger.signTypedMessage(id, JSON.stringify(data))
	}
}, {
	address: addressHex,networkVersion: chainID
});
window.web3.setProvider = function () {console.debug('Tiger Wallet - overrode web3.setProvider')}
window.web3.eth.defaultAccount = addressHex
window.web3.version.getNetwork = function (cb) {cb(null, chainID)}
window.web3.eth.getCoinbase = function (cb) {return cb(null, addressHex)}