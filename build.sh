#!/bin/sh

GIT_ROOT=$(cd $(dirname ${0}); pwd) 
cd ${GIT_ROOT}

mvn -Dmaven.test.skip=true clean package --settings settings.xml