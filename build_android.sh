#!/bin/bash

NDK_PATH=/Users/bottle/Library/Android/sdk/ndk/23.1.7779620
HOST_PLATFORM=darwin-x86_64
API=23

TOOLCHAINS="$NDK_PATH/toolchains/llvm/prebuilt/$HOST_PLATFORM"
SYSROOT="$NDK_PATH/toolchains/llvm/prebuilt/$HOST_PLATFORM/sysroot"
CFLAG="-D__ANDROID_API__=$API -Os -fPIC -DANDROID "
LDFLAG="-lc -lm -ldl -llog "


PREFIX=`pwd`/android-build

CONFIG_LOG_PATH=${PREFIX}/log

COMMON_OPTIONS=

CONFIGURATION=

build() {
    APP_ABI=$1
    echo "======== > Start build $APP_ABI"
    case ${APP_ABI} in
    armeabi-v7a)
    ARCH="arm"
    CPU="armv7-a"
    MARCH="armv7-a"
    TARGET=armv7a-linux-androideabi
    CC="$TOOLCHAINS/bin/$TARGET$API-clang"
    CXX="$TOOLCHAINS/bin/$TARGET$API-clang++"
    LD="$TOOLCHAINS/bin/$TARGET$API-clang"
    # 交叉编译工具前缀
    CROSS_PREFIX="$TOOLCHAINS/bin/arm-linux-androideabi-"
    EXTRA_CFLAGS="$CFLAG -mfloat-abi=softfp -mfpu=vfp -marm -march=$MARCH "
    EXTRA_LDFLAGS="$LDFLAG"
    EXTRA_OPTIONS="--enable-neon --cpu=$CPU "
    ;;
    arm64-v8a)
    ARCH="aarch64"
    TARGET=$ARCH-linux-android
    CC="$TOOLCHAINS/bin/$TARGET$API-clang"
    CXX="$TOOLCHAINS/bin/$TARGET$API-clang++"
    LD="$TOOLCHAINS/bin/$TARGET$API-clang"
    CROSS_PREFIX="$TOOLCHAINS/bin/$TARGET-"
    EXTRA_CFLAGS="$CFLAG"
    EXTRA_LDFLAGS="$LDFLAG"
    EXTRA_OPTIONS=""
    ;;
    x86)
    ARCH="x86"
    CPU="i686"
    MARCH="i686"
    TARGET=i686-linux-android
    CC="$TOOLCHAINS/bin/$TARGET$API-clang"
    CXX="$TOOLCHAINS/bin/$TARGET$API-clang++"
    LD="$TOOLCHAINS/bin/$TARGET$API-clang"
    CROSS_PREFIX="$TOOLCHAINS/bin/$TARGET-"
    #EXTRA_CFLAGS="$CFLAG -march=$MARCH -mtune=intel -mssse3 -mfpmath=sse -m32"
    EXTRA_CFLAGS="$CFLAG -march=$MARCH  -mssse3 -mfpmath=sse -m32"
    EXTRA_LDFLAGS="$LDFLAG"
    EXTRA_OPTIONS="--cpu=$CPU "
    ;;
    x86_64)
    ARCH="x86_64"
    CPU="x86-64"
    MARCH="x86_64"
    TARGET=$ARCH-linux-android
    CC="$TOOLCHAINS/bin/$TARGET$API-clang"
    CXX="$TOOLCHAINS/bin/$TARGET$API-clang++"
    LD="$TOOLCHAINS/bin/$TARGET$API-clang"
    CROSS_PREFIX="$TOOLCHAINS/bin/$TARGET-"
    #EXTRA_CFLAGS="$CFLAG -march=$CPU -mtune=intel -msse4.2 -mpopcnt -m64"
    EXTRA_CFLAGS="$CFLAG -march=$CPU -msse4.2 -mpopcnt -m64"
    EXTRA_LDFLAGS="$LDFLAG"
    EXTRA_OPTIONS="--cpu=$CPU "
    ;;
    esac
    
    echo "-------- > Start clean workspace"
    make clean
    
    echo "-------- > Start build configuration"
    CONFIGURATION="$COMMON_OPTIONS"
    CONFIGURATION="$CONFIGURATION --logfile=$CONFIG_LOG_PATH/config_$APP_ABI.log"
    CONFIGURATION="$CONFIGURATION --prefix=$PREFIX"
    CONFIGURATION="$CONFIGURATION --libdir=$PREFIX/libs/$APP_ABI"
    CONFIGURATION="$CONFIGURATION --incdir=$PREFIX/includes/$APP_ABI"
    CONFIGURATION="$CONFIGURATION --pkgconfigdir=$PREFIX/pkgconfig/$APP_ABI"
    CONFIGURATION="$CONFIGURATION --cross-prefix=$CROSS_PREFIX"
    CONFIGURATION="$CONFIGURATION --arch=$ARCH"
    CONFIGURATION="$CONFIGURATION --sysroot=$SYSROOT"
    CONFIGURATION="$CONFIGURATION --cc=$CC"
    CONFIGURATION="$CONFIGURATION --cxx=$CXX"
    CONFIGURATION="$CONFIGURATION --ld=$LD"
    # nm 和 strip
    CONFIGURATION="$CONFIGURATION --nm=$TOOLCHAINS/bin/llvm-nm"
    CONFIGURATION="$CONFIGURATION --strip=$TOOLCHAINS/bin/llvm-strip"
    CONFIGURATION="$CONFIGURATION $EXTRA_OPTIONS"
    
    echo "-------- > Start config makefile with $CONFIGURATION --extra-cflags=${EXTRA_CFLAGS} --extra-ldflags=${EXTRA_LDFLAGS}"
    ./configure ${CONFIGURATION} \
    --extra-cflags="$EXTRA_CFLAGS" \
    --extra-ldflags="$EXTRA_LDFLAGS"
    
    echo "-------- > Start make $APP_ABI with -j1"
    make -j1
    
    echo "-------- > Start install $APP_ABI"
    make install
    echo "++++++++ > make and install $APP_ABI complete."
    
}

build_all() {
    
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-gpl"
    
    COMMON_OPTIONS="$COMMON_OPTIONS --target-os=android"
    
    COMMON_OPTIONS="$COMMON_OPTIONS --disable-static"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-shared"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-protocols"
    
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-cross-compile"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-optimizations"
    COMMON_OPTIONS="$COMMON_OPTIONS --disable-debug"
    

    COMMON_OPTIONS="$COMMON_OPTIONS --enable-small"
    COMMON_OPTIONS="$COMMON_OPTIONS --disable-doc"
    
    COMMON_OPTIONS="$COMMON_OPTIONS --disable-programs"  # do not build command line programs
    COMMON_OPTIONS="$COMMON_OPTIONS --disable-ffmpeg"    # disable ffmpeg build
    COMMON_OPTIONS="$COMMON_OPTIONS --disable-ffplay"    # disable ffplay build
    COMMON_OPTIONS="$COMMON_OPTIONS --disable-ffprobe"   # disable ffprobe build
    COMMON_OPTIONS="$COMMON_OPTIONS --disable-symver"
    COMMON_OPTIONS="$COMMON_OPTIONS --disable-network"
    COMMON_OPTIONS="$COMMON_OPTIONS --disable-x86asm"
    COMMON_OPTIONS="$COMMON_OPTIONS --disable-asm"

    COMMON_OPTIONS="$COMMON_OPTIONS --enable-pthreads"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-mediacodec"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-jni"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-zlib"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-pic"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-muxer=flv"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-muxer=flv"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-muxer=mov"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-muxer=avi"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-muxer=mpegts"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-decoder=flac"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-decoder=h264"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-decoder=png"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-decoder=mjpeg"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-decoder=mpeg4"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-decoder=jpeg"        
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-decoder=png"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-decoder=vorbis"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-decoder=opus"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-decoder=flac"

    COMMON_OPTIONS="$COMMON_OPTIONS --enable-encoder=flac"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-encoder=h264"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-encoder=png"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-encoder=mjpeg"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-encoder=mpeg4"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-encoder=jpeg"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-encoder=png"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-encoder=vorbis"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-encoder=opus"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-encoder=flac"

    COMMON_OPTIONS="$COMMON_OPTIONS --enable-libopencv"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-libopenh264"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-libopenjpeg"

    COMMON_OPTIONS="$COMMON_OPTIONS --enable-filter=scale"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-filter=rotate"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-filter=blur"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-filter=crop"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-filter=overlay"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-filter=drawtext"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-filter=trim"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-filter=fps"






    COMMON_OPTIONS="$COMMON_OPTIONS --enable-parser=h264"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-parser=hevc"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-parser=aac"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-demuxer=flv"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-demuxer=mov"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-demuxer=avi"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-demuxer=mpegts"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-protocol=file"
    COMMON_OPTIONS="$COMMON_OPTIONS --enable-protocol=hls"

    
    
    
    
    
    echo "COMMON_OPTIONS=$COMMON_OPTIONS"
    echo "PREFIX=$PREFIX"
    echo "CONFIG_LOG_PATH=$CONFIG_LOG_PATH"
    mkdir -p ${CONFIG_LOG_PATH}
    build "armeabi-v7a"
    build "arm64-v8a"
    build "x86"
    build "x86_64"
    
}

echo "-------- Start --------"
build_all
echo "-------- End --------"
