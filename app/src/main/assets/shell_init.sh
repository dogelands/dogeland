#
# dogeland shell init
#
export START_DIR=$({START_DIR})
export SDCARD_PATH=$({SDCARD_PATH})
export PACKAGE_NAME=$({PACKAGE_NAME})
export TOOLKIT=$START_DIR/usr/bin
export TMPDIR=$START_DIR/cache
export PREFIX=$START_DIR/usr
export LD_LIBRARY_PATH="$LD_LIBRARY_PATH:$PREFIX/lib/"
export PATH="/system/bin:$TOOLKIT"
export DATA2_DIR="$SDCARD_PATH/Android/data/$PACKAGE_NAME/files/"
export CONFIG_DIR="$DATA2_DIR/config/"

export PROOT_TMP_DIR="$TMPDIR/"
export platform=$(sh $TOOLKIT/cli.sh platform)
export cmd=$(cat $CONFIG_DIR/cmd.conf)
export rootfs=$(cat $CONFIG_DIR/rootfs.conf)
if [[ -f "$TOOLKIT/install_bin_done" ]]; then
  echo "">/dev/null
  else
  sh $TOOLKIT/install_bin.sh
fi
if [[ -f "$1" ]]; then
    cd $PREFIX
    . "$1"
else
    echo "未指定命令"
    exit
fi