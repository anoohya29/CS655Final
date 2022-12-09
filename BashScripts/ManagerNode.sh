#Script to be executed on manager node.
sudo apt update
# install java runtime environment
sudo apt install default-jre  
# install java compiler
sudo apt install default-jdk 
# we need to insatll nginx
sudo apt install nginx
# delete old files
sudo rm main.zip
sudo rm -r CS655Final-main
# download project files.
sudo wget --no-check-certificate --no-cache --no-cookies https://github.com/anoohya29/CS655Final/archive/refs/heads/main.zip
# extract project files
sudo unzip main.zip
# create the configuration file for nginx
sudo touch /etc/nginx/conf.d/static-naice-me.conf
# start writing configuration for nginx
sudo echo 'server {' > /etc/nginx/conf.d/static-naice-me.conf
sudo echo '  server_name pcvm3-13.geni.uchicago.edu;' >> /etc/nginx/conf.d/static-naice-me.conf
sudo echo '  root '`pwd`'/CS655Final-main/Web;' >> /etc/nginx/conf.d/static-naice-me.conf
sudo echo '  index index.html;' >> /etc/nginx/conf.d/static-naice-me.conf
sudo echo '  location ~* ^.+\.(jpg|jpeg|gif|png|ico|css|js|pdf|txt){' >> /etc/nginx/conf.d/static-naice-me.conf
sudo echo '    root '`pwd`'/CS655Final-main/Web;' >> /etc/nginx/conf.d/static-naice-me.conf
sudo echo '  }' >> /etc/nginx/conf.d/static-naice-me.conf
sudo echo '}' >> /etc/nginx/conf.d/static-naice-me.conf

# restart the nginx
sudo nginx -s reload  
# Change directory to manager folder.
cd CS655Final-main/ManagerNode
# complie the java files
sudo javac NodeManager.java
# start the manager
sudo java NodeManager 58888 info
