#!/bin/sh

LOGFILE=/home/zettarus/sync.log

pidof rsync > /dev/null
if [ $? -eq 0 ]; then
        echo $$ `date` "Already running." >> $LOGFILE
else
        echo $$ `date` "Syncing *.jpg" >> $LOGFILE
        rsync -az --remove-source-files -e ssh rus:/var/motion/current/*.jpg /home/zettarus/motion/ >> $LOGFILE
        #echo $$ `date` "Syncing *.swf" >> $LOGFILE
        #rsync -az --remove-source-files -e ssh rus:/var/motion/current/*.swf /home/zettarus/motion/ >> $LOGFILE
fi

