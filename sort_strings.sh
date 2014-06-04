#!/bin/sh
for i in src/ch/imedias/rscc/Bundle*
do
	sort $i>tmp
	mv tmp $i
done
