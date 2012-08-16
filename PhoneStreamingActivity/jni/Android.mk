LOCAL_PATH := $(call my-dir)
LOCAL_PATH := /home/kesten/VCP/Git/opnecv-orbotix/spheroTracker/features/PhoneStreamingActivity/jni
include $(CLEAR_VARS)

include /home/kesten/Android-OpenCV-2.4.2/OpenCV-2.4.2/sdk/native/jni/OpenCV.mk

LOCAL_MODULE    := native_sample
LOCAL_SRC_FILES := HoughCircles.cpp
LOCAL_LDLIBS +=  -llog -ldl

include $(BUILD_SHARED_LIBRARY)
