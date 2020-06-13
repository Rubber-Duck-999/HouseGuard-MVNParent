#!/bin/sh

cd $HOME/Documents/HouseGuard-MVNParent

git pull

mvn clean

mvn install

if [ -f DatabaseManager/target/DatabaseManager* ];
then
    echo "DBM File found"
    if [ -f $HOME/Documents/Deploy/DatabaseManager.jar ];
    then
        echo "DBM old removed"
        rm -f $HOME/Documents/Deploy/DatabaseManager.jar
    fi
    mv DatabaseManager/target/DatabaseManager* $HOME/Documents/Deploy/DatabaseManager.jar
fi

if [ -f UserPanel/target/UserPanel* ];
then
    echo "UP File found"
    if [ -f $HOME/Documents/Deploy/UserPanel.jar ];
    then
        echo "UP old removed"
        rm -f $HOME/Documents/Deploy/UserPanel.jar
    fi
    mv UserPanel/target/UserPanel* $HOME/Documents/Deploy/UserPanel.jar
fi

mvn clean