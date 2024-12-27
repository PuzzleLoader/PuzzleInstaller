pathSep=":"

if [[ "$OSTYPE" =~ "msys" ]]; then
    pathSep=";"
fi

# shellcheck disable=SC2154
java -Xmx4G -Xms2G -cp "@classPath" @mainClass