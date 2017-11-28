mkdir etc
cd etc
del server_ks
del client_ks
del server_key.cer
echo nihao
#
keytool -genkey -v -alias bluedash-ssl-demo-server -keyalg RSA -keystore ./server_ks -dname "CN=localhost,OU=cn,O=cn,L=cn,ST=cn,C=cn" -storepass server -keypass 123123
keytool -genkey -v -alias bluedash-ssl-demo-client -keyalg RSA -keystore ./client_ks -dname "CN=localhost,OU=cn,O=cn,L=cn,ST=cn,C=cn" -storepass client -keypass 456456
keytool -export -alias bluedash-ssl-demo-server -keystore ./server_ks -file server_key.cer -storepass server
keytool -import -trustcacerts -alias bluedash-ssl-demo-server -file ./server_key.cer -keystore ./client_ks -storepass client
pause
