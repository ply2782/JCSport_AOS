LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := NDK
LOCAL_SRC_FILES := ffmpeg.c
LOCAL_LDLIBS := -llog
LOCAL_SHARED_LIBRARIES := \
libavformat_1 libavcodec_1 libswscale_1 libavutil_1 libswresample_1 libavfilter_1 libpostproc_1 libavdevice_1 \
#libavformat_2 libavcodec_2 libswscale_2 libavutil_2 libswresample_2 libavfilter_2 libpostproc_2 libavdevice_2 \
#libavformat_3 libavcodec_3 libswscale_3 libavutil_3 libswresample_3 libavfilter_3 libpostproc_3 libavdevice_3 \
#libavformat_4 libavcodec_4 libswscale_4 libavutil_4 libswresample_4 libavfilter_4 libpostproc_4 libavdevice_4
LOCAL_CFLAGS = -DSTDC_HEADERS
include $(BUILD_SHARED_LIBRARY)
$(call import-add-path, /Users/bottle/Desktop/App/Android/JCSports/app)
$(call import-module, libs)
