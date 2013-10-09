#!/bin/bash

#parameters
# the directory containing the IndoWordnet database dump
IWN_DIR=$1
#Output directory where the data is generated in a format as required by METEOR
#$OUT_DIR/meteor_data will contain the synonym resources to be used in METEOR
OUT_DIR=$2
# NOTE: Manipuri needs some manual cleanup currently, since there is an 'N' in the idx file after the synset ids. This simple cleanup applied for other languages cannot be done for Manipuri since it is written in the Devanagari script


mkdir -p $OUT_DIR/iwn_data $OUT_DIR/meteor_data
cd $OUT_DIR/iwn_data

for f in `find $IWN_DIR -name '*zip'`
do
 #echo $f `basename $f`
 unzip $f
done

# use 2-letter ISO 639 codes with invented codes for languages not having a code, indicated by capital letter in the code
mv ass as
mv ben bn
mv bod bD
mv guj gu
mv hin hi
mv kan kn
mv kas ks
mv kon kK
mv mal ml
mv man mP
mv mar mr
mv nep ne
mv ori or
mv pan pa
mv san sa
mv tam ta 
mv tel te
mv urd ur

for f in `ls`
do
 cat $f/{adj.idx,noun.idx,adv.idx,verb.idx} > $f/all.idx
 if [ $f != 'mP' ]
 then  
    sed 's, ,\n,' $f/all.idx  | sed 's,N, ,'  > ../meteor_data/$f.synsets
 else    
    sed 's, ,\n,' $f/all.idx  > ../meteor_data/$f.synsets
 fi  
    touch ../meteor_data/$f.exceptions
    touch ../meteor_data/$f.relations
done
