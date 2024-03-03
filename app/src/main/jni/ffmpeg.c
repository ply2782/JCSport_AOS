#include "com_jc_jcsports_utils_videotrim_NDK.h"
#include <android/log.h>
#include <unistd.h>
#include <jni.h>
#include <string.h>
#include <stdio.h>
#include <sys/time.h>
#include <pthread.h>
#include <stdlib.h>
#include "../../../libs/includes/armeabi-v7a/libavutil/avassert.h"
#include "../../../libs/includes/armeabi-v7a/libavutil/file.h"
#include "../../../libs/includes/armeabi-v7a/libavutil/avutil.h"
#include "../../../libs/includes/armeabi-v7a/libavformat/avformat.h"
#include "../../../libs/includes/armeabi-v7a/libavcodec/avcodec.h"
#include "../../../libs/includes/armeabi-v7a/libavutil/avutil.h"
#include "../../../libs/includes/armeabi-v7a/libavutil//dict.h"
#include "../../../libs/includes/armeabi-v7a/libavutil/imgutils.h"
#include "../../../libs/includes/armeabi-v7a/libswscale/swscale.h"
#include "../../../libs/includes/armeabi-v7a/libavfilter/avfilter.h"
#include "../../../libs/includes/armeabi-v7a/libavutil/parseutils.h"
#include "../../../libs/includes/armeabi-v7a/libavfilter/buffersink.h"
#include "../../../libs/includes/armeabi-v7a/libavfilter/buffersrc.h"
#include "../../../libs/includes/armeabi-v7a/libavutil/opt.h"
#include "../../../libs/includes/armeabi-v7a/libavutil/samplefmt.h"
#include "../../../libs/includes/armeabi-v7a/libavutil/channel_layout.h"
#include "../../../libs/includes/armeabi-v7a/libavutil/mathematics.h"
#include "../../../libs/includes/armeabi-v7a/libavutil/timestamp.h"
#include "../../../libs/includes/armeabi-v7a/libswresample/swresample.h"
#include "../../../libs/includes/armeabi-v7a/libavutil/frame.h"
#include "../../../libs/includes/armeabi-v7a/libavutil/pixdesc.h"
#include <pthread.h>

#define STBI_MSC_SECURE_CRT
#define STB_IMAGE_WRITE_IMPLEMENTATION

#include "../../../../app/libs/stb/stb_image_write.h"
#include "libavutil/display.h"

#define LOG_TAG "debug"
#define LOGI(...) __android_log_print(4, LOG_TAG, __VA_ARGS__);



/** */
//todo ppm 이미지 만드는 코드
/** void SaveFrameAsPPM(const uint8_t* buffer, int width, int height, int frameNumber, const char* documentPath) {
    char filename[256];
    snprintf(filename, sizeof(filename), "%s_frame%d.ppm", documentPath, frameNumber);
    FILE* file = fopen(filename, "wb");
    if (!file) {
        LOGI("Failed to open file for writing: %s\n", filename);
        return;
    }

    // 수정된 부분 시작
    int rotatedWidth = height; // 회전된 이미지의 너비
    int rotatedHeight = width; // 회전된 이미지의 높이
    uint8_t* rotatedBuffer = (uint8_t*)malloc(rotatedWidth * rotatedHeight * 3); // 회전된 이미지를 저장할 버퍼

    // -90도 회전 수행
    for (int y = 0; y < height; ++y) {
        for (int x = 0; x < width; ++x) {
            int rotatedX = height - y - 1;
            int rotatedY = x;
            int originalIndex = (y * width + x) * 3;
            int rotatedIndex = (rotatedY * rotatedWidth + rotatedX) * 3;
            rotatedBuffer[rotatedIndex] = buffer[originalIndex];
            rotatedBuffer[rotatedIndex + 1] = buffer[originalIndex + 1];
            rotatedBuffer[rotatedIndex + 2] = buffer[originalIndex + 2];
        }
    }
    // 수정된 부분 끝

    fprintf(file, "P6\n%d %d\n255\n", rotatedWidth, rotatedHeight);
    fwrite(rotatedBuffer, 1, rotatedWidth * rotatedHeight * 3, file);

    fclose(file);
    free(rotatedBuffer); // 회전된 이미지를 저장하기 위해 동적으로 할당한 메모리를 해제합니다.
}*/


/** */
void SaveFrameAsPNG(const uint8_t *buffer, int width, int height, int frameNumber,
                    const char *documentPath, const char *currentTime) {

    char filename[256];
    snprintf(filename, sizeof(filename), "%sthumbNail%s%d.png", documentPath, currentTime,
             frameNumber);

    // 회전된 이미지의 너비와 높이 계산
    int rotatedWidth = height;
    int rotatedHeight = width;

    // 회전된 이미지를 저장할 버퍼 동적 할당
    uint8_t *rotatedBuffer = (uint8_t *) malloc(rotatedWidth * rotatedHeight * 3);

    // -90도 회전 수행
    for (int y = 0; y < height; ++y) {
        for (int x = 0; x < width; ++x) {
            int rotatedX = height - y - 1;
            int rotatedY = x;
            int originalIndex = (y * width + x) * 3;
            int rotatedIndex = (rotatedY * rotatedWidth + rotatedX) * 3;
            rotatedBuffer[rotatedIndex] = buffer[originalIndex];
            rotatedBuffer[rotatedIndex + 1] = buffer[originalIndex + 1];
            rotatedBuffer[rotatedIndex + 2] = buffer[originalIndex + 2];
        }
    }

    // PNG 파일로 회전된 이미지 저장
    int ret = stbi_write_png(filename, rotatedWidth, rotatedHeight, 3, rotatedBuffer,
                             rotatedWidth * 3);
    if (ret == 0) {
        LOGI("Failed to save PNG file: %s\n", filename);
    }

    // 회전된 이미지를 저장하기 위해 동적으로 할당한 메모리 해제
    free(rotatedBuffer);
}

struct ThreadArgs {
    AVFormatContext *formatCtx;
    AVCodecContext *codecCtx;
    AVFrame *frameRGB;
    AVFrame *frame;
    struct SwsContext *swsCtx;
    int videoStreamIndex;
    int frameNumber;
    char *dsc_path;
    char dateTimeString[15];
};

void *processFrames(void *arg) {
    struct ThreadArgs *threadArgs = (struct ThreadArgs *) arg;
    AVFormatContext *formatCtx = threadArgs->formatCtx;
    AVCodecContext *codecCtx = threadArgs->codecCtx;
    AVFrame *frameRGB = threadArgs->frameRGB;
    AVFrame *frame = threadArgs->frame;
    struct SwsContext *swsCtx = threadArgs->swsCtx;
    int videoStreamIndex = threadArgs->videoStreamIndex;
    int frameNumber = threadArgs->frameNumber;
    char *dsc_path = threadArgs->dsc_path;
    char *dateTimeString = threadArgs->dateTimeString;

    AVPacket packet;

    while (av_read_frame(formatCtx, &packet) >= 0) {
        if (packet.stream_index == videoStreamIndex) {
            if (avcodec_send_packet(codecCtx, &packet) == 0) {
                int ret;
                while ((ret = avcodec_receive_frame(codecCtx, frame)) == 0) {
                    sws_scale(swsCtx, (const uint8_t *const *) frame->data, frame->linesize, 0,
                              codecCtx->height, frameRGB->data, frameRGB->linesize);
                    SaveFrameAsPNG(frameRGB->data[0], codecCtx->width,
                                   codecCtx->height,
                                   frameNumber++, dsc_path, dateTimeString);
                }
                if (codecCtx->frame_num == 1)
                    break;
                if (ret != AVERROR(EAGAIN) && ret != AVERROR_EOF) {
                    printf("Error during frame decoding.\n");
                    break;
                }
            }
        }
        av_packet_unref(&packet);
    }

    return NULL;
}


JNIEXPORT jstring JNICALL
Java_com_jc_jcsports_utils_videotrim_NDK_videoFileToPpm(JNIEnv *env, jobject thiz, jstring input,
                                                        jstring output) {

    const char *src_path = (*env)->GetStringUTFChars(env, input, NULL);
    const char *dsc_path = (*env)->GetStringUTFChars(env, output, NULL);
    int videoStreamIndex = -1;
    AVCodecParameters *codecParams = NULL;
    AVCodec *codec = NULL;
    AVCodecContext *codecCtx = NULL;
    AVFrame *frame = NULL;
    char *charResult = "fail";

    avformat_network_init();


    AVFormatContext *formatCtx = avformat_alloc_context();
    if (avformat_open_input(&formatCtx, src_path, NULL, NULL) != 0) {
        LOGI("Failed to open input file: %s\n", src_path);
        return charResult;
    }
    if (avformat_find_stream_info(formatCtx, NULL) < 0) {
        LOGI("Failed to retrieve stream information.\n");
        avformat_close_input(&formatCtx);
        return charResult;
    }

    for (int i = 0; i < formatCtx->nb_streams; i++) {
        if (formatCtx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            videoStreamIndex = i;
            codecParams = formatCtx->streams[i]->codecpar;
            break;
        }
    }
    if (videoStreamIndex == -1) {
        LOGI("No video stream found in the input file.\n");
        avformat_close_input(&formatCtx);
        return charResult;
    }
    codec = avcodec_find_decoder(codecParams->codec_id);
    if (!codec) {
        LOGI("Unsupported codec.\n");
        avformat_close_input(&formatCtx);
        return charResult;
    }
    codecCtx = avcodec_alloc_context3(codec);
    if (!codecCtx) {
        LOGI("Failed to allocate codec context.\n");
        avformat_close_input(&formatCtx);
        return charResult;
    }

    if (avcodec_parameters_to_context(codecCtx, codecParams) < 0) {
        LOGI("Failed to copy codec parameters to codec context.\n");
        avcodec_free_context(&codecCtx);
        avformat_close_input(&formatCtx);
        return charResult;
    }

    if (avcodec_open2(codecCtx, codec, NULL) < 0) {
        LOGI("Failed to open codec.\n");
        avcodec_free_context(&codecCtx);
        avformat_close_input(&formatCtx);
        return charResult;
    }
    frame = av_frame_alloc();
    if (!frame) {
        LOGI("Failed to allocate frame.\n");
        avcodec_free_context(&codecCtx);
        avformat_close_input(&formatCtx);
        return charResult;
    }
    AVFrame *frameRGB = av_frame_alloc();
    if (!frameRGB) {
        LOGI("Failed to allocate RGB frame.\n");
        av_frame_free(&frame);
        avcodec_free_context(&codecCtx);
        avformat_close_input(&formatCtx);
        return charResult;
    }
    int numBytes = av_image_get_buffer_size(AV_PIX_FMT_RGB24, codecCtx->width, codecCtx->height, 1);
    uint8_t *buffer = (uint8_t *) av_malloc(numBytes);
    av_image_fill_arrays(frameRGB->data, frameRGB->linesize, buffer, AV_PIX_FMT_RGB24,
                         codecCtx->width, codecCtx->height, 1);
    struct SwsContext *swsCtx = sws_getContext(codecCtx->width, codecCtx->height, codecCtx->pix_fmt,
                                               codecCtx->width, codecCtx->height, AV_PIX_FMT_RGB24,
                                               SWS_BILINEAR, NULL, NULL, NULL);
    int frameNumber = 0;
    time_t currentTime = time(NULL);
    char dateTimeString[15];
    strftime(dateTimeString, sizeof(dateTimeString), "%Y%m%d%H%M%S", localtime(&currentTime));

    // 스레드에 전달할 인자를 설정합니다.
    struct ThreadArgs threadArgs;
    threadArgs.formatCtx = formatCtx;
    threadArgs.codecCtx = codecCtx;
    threadArgs.frameRGB = frameRGB;
    threadArgs.swsCtx = swsCtx;
    threadArgs.videoStreamIndex = videoStreamIndex;
    threadArgs.frameNumber = frameNumber;
    threadArgs.dsc_path = dsc_path;
    threadArgs.frame = frame;
    strcpy(threadArgs.dateTimeString, dateTimeString);

    pthread_t thread_id;
    int result = pthread_create(&thread_id, NULL, processFrames, (void *) &threadArgs);
    if (result != 0) {
        printf("스레드 생성 실패\n");
        return charResult;
    }
    result = pthread_join(thread_id, NULL);
    if (result != 0) {
        printf("스레드 조인 실패\n");
        return charResult;
    }
    sws_freeContext(swsCtx);
    av_free(buffer);
    av_frame_free(&frameRGB);
    av_frame_free(&frame);
    avcodec_free_context(&codecCtx);
    avformat_close_input(&formatCtx);

    char filename[256];
    snprintf(filename, sizeof(filename), "%sthumbNail%s%d.png", dsc_path, dateTimeString, 0);
    charResult = (*env)->NewStringUTF(env, filename);
    return charResult;
}

JNIEXPORT jint
JNICALL
Java_com_jc_jcsports_utils_videotrim_NDK_ffmpeg(JNIEnv *env, jobject thiz, jstring input,
                                                jstring output, jint start_ms,
                                                jint end_ms) {


    //todo : 시작시간과 끝나는 시간 범위 안에서 비디오와 오디오 같이 편집하는 코드
    const char *src_path = (*env)->GetStringUTFChars(env, input, NULL);
    const char *dst_path = (*env)->GetStringUTFChars(env, output, NULL);
    const char *input_filename = src_path;
    const char *output_filename = dst_path;
    const AVDictionaryEntry *tag = NULL;


    avformat_network_init();

    // Open input file
    AVFormatContext *input_ctx = NULL;
    if (avformat_open_input(&input_ctx, input_filename, NULL, NULL) < 0) {
        LOGI("Error opening input file\n");
        return -1;
    }

    // Retrieve stream information
    if (avformat_find_stream_info(input_ctx, NULL) < 0) {
        LOGI("Error finding stream information\n");
        return -1;
    }

    while ((tag = av_dict_iterate(input_ctx->metadata, tag)))
        LOGI("%s=%s\n", tag->key, tag->value);

    // Find video stream
    int video_stream_index = -1;
    for (int i = 0; i < input_ctx->nb_streams; i++) {
        if (input_ctx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            video_stream_index = i;
            break;
        }
    }


    if (video_stream_index == -1) {
        LOGI("No video stream found in input file\n");
        return -1;
    }

    // Find audio stream
    int audio_stream_index = -1;
    for (int i = 0; i < input_ctx->nb_streams; i++) {
        if (input_ctx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            audio_stream_index = i;
            break;
        }
    }

    if (audio_stream_index == -1) {
        LOGI("No audio stream found in input file\n");
        return -1;
    }


    // Open output file
    AVFormatContext *output_ctx = NULL;
    if (avformat_alloc_output_context2(&output_ctx, NULL, NULL, output_filename) < 0) {
        LOGI("Error creating output context\n");
        return -1;
    }

    // Create video output stream
    AVStream *video_output_stream = avformat_new_stream(output_ctx, NULL);
    if (!video_output_stream) {
        LOGI("Failed to create video output stream\n");
        return -1;
    }

    AVStream *video_input_stream = input_ctx->streams[video_stream_index];
    if (avcodec_parameters_copy(video_output_stream->codecpar, video_input_stream->codecpar) < 0) {
        LOGI("Failed to copy video codec parameters\n");
        return -1;
    }

    // Create audio output stream
    AVStream *audio_output_stream = avformat_new_stream(output_ctx, NULL);
    if (!audio_output_stream) {
        LOGI("Failed to create audio output stream\n");
        return -1;
    }

    AVStream *audio_input_stream = input_ctx->streams[audio_stream_index];
    if (avcodec_parameters_copy(audio_output_stream->codecpar, audio_input_stream->codecpar) < 0) {
        LOGI("Failed to copy audio codec parameters\n");
        return -1;
    }


    // Open output file
    if (!(output_ctx->oformat->flags & AVFMT_NOFILE)) {
        if (avio_open(&output_ctx->pb, output_filename, AVIO_FLAG_WRITE) < 0) {
            LOGI("Error opening output file\n");
            return -1;
        }
    }

    // Write output file header
    AVDictionary *codecOptions = NULL;
    if (avformat_write_header(output_ctx, &codecOptions) < 0) {
        LOGI("Error writing output file header\n");
        return -1;
    }


    int64_t start_time = av_rescale_q(start_ms * AV_TIME_BASE, AV_TIME_BASE_Q,
                                      input_ctx->streams[video_stream_index]->time_base);
    int64_t end_time = av_rescale_q(end_ms * AV_TIME_BASE, AV_TIME_BASE_Q,
                                    input_ctx->streams[video_stream_index]->time_base);
    int64_t start_time_a = av_rescale_q(start_ms * AV_TIME_BASE, AV_TIME_BASE_Q,
                                        input_ctx->streams[audio_stream_index]->time_base);
    int64_t end_time_a = av_rescale_q(end_ms * AV_TIME_BASE, AV_TIME_BASE_Q,
                                      input_ctx->streams[audio_stream_index]->time_base);

    av_seek_frame(input_ctx, video_stream_index, start_time, AVSEEK_FLAG_BACKWARD);

    AVPacket pkt;
    while (av_read_frame(input_ctx, &pkt) >= 0) {
        if (pkt.stream_index == video_stream_index) {
            if (pkt.pts < end_time) {

                pkt.pts = av_rescale_q_rnd((pkt.pts - start_time), video_input_stream->time_base,
                                           video_output_stream->time_base,
                                           AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX);

                pkt.dts = av_rescale_q_rnd((pkt.dts - start_time), video_input_stream->time_base,
                                           video_output_stream->time_base,
                                           AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX);

                pkt.duration = av_rescale_q(pkt.duration, video_input_stream->time_base,
                                            video_output_stream->time_base);

                pkt.pos = -1;
                if ((av_interleaved_write_frame(output_ctx, &pkt)) < 0) {
                    break;
                }
            }

        } else if (pkt.stream_index == audio_stream_index) {

            if (pkt.pts < end_time_a) {

                pkt.pts = av_rescale_q_rnd((pkt.pts - start_time_a), audio_input_stream->time_base,
                                           audio_output_stream->time_base,
                                           AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX);

                pkt.dts = av_rescale_q_rnd((pkt.dts - start_time_a), audio_input_stream->time_base,
                                           audio_output_stream->time_base,
                                           AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX);

                pkt.duration = av_rescale_q(pkt.duration, audio_input_stream->time_base,
                                            audio_output_stream->time_base);

                pkt.pos = -1;

                if ((av_interleaved_write_frame(output_ctx, &pkt)) < 0) {
                    break;
                }
            }
        }

        av_packet_unref(&pkt);
        if (pkt.pts > end_time)
            break;
    }
    av_write_trailer(output_ctx);

    avformat_close_input(&input_ctx);
    if (output_ctx != NULL && !(output_ctx->oformat->flags & AVFMT_NOFILE))
        avio_close(output_ctx->pb);
    avformat_free_context(output_ctx);

    (*env)->ReleaseStringUTFChars(env, input, src_path);
    (*env)->ReleaseStringUTFChars(env, output, dst_path);

    return 0;
}



/** */
JNIEXPORT jint JNICALL
Java_com_jc_jcsports_utils_videotrim_NDK_mp4Andmp3Remux(JNIEnv *env, jobject thiz, jstring input,
                                                        jstring output, jint start_ms,
                                                        jint end_ms) {

    const char *src_path = (*env)->GetStringUTFChars(env, input, NULL);
    const char *dst_path = (*env)->GetStringUTFChars(env, output, NULL);
    const char *inputAudioFile = "/storage/emulated/0/Music/Samsung/Over_the_Horizon.mp3";
    const char *videoFile = src_path;
    const char *audioFile = inputAudioFile;
    const char *outputFile = dst_path;


    avformat_network_init();

    AVFormatContext *videoFormatCtx = avformat_alloc_context();
    if (avformat_open_input(&videoFormatCtx, videoFile, NULL, NULL) != 0) {
        LOGI("Failed to open video file\n");
        return -1;
    }

    AVFormatContext *audioFormatCtx = avformat_alloc_context();
    if (avformat_open_input(&audioFormatCtx, audioFile, NULL, NULL) != 0) {
        LOGI("Failed to open audio file\n");
        return -1;
    }

    if (avformat_find_stream_info(videoFormatCtx, NULL) < 0) {
        LOGI("Failed to retrieve input video stream information\n");
        return -1;
    }

    if (avformat_find_stream_info(audioFormatCtx, NULL) < 0) {
        LOGI("Failed to retrieve input audio stream information\n");
        return -1;
    }

    AVStream *videoStream = NULL;
    AVStream *audioStream = NULL;
    int videoStreamIndex = -1;
    int audioStreamIndex = -1;

    for (int i = 0; i < videoFormatCtx->nb_streams; i++) {
        if (videoFormatCtx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            videoStream = videoFormatCtx->streams[i];
            videoStreamIndex = i;
            break;
        }
    }

    for (int i = 0; i < audioFormatCtx->nb_streams; i++) {
        if (audioFormatCtx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            audioStream = audioFormatCtx->streams[i];
            audioStreamIndex = i;
            break;
        }
    }


    AVFormatContext *outputFormatCtx = NULL;
    if (avformat_alloc_output_context2(&outputFormatCtx, NULL, NULL, outputFile) < 0) {
        LOGI("Failed to allocate output format context\n");
        return -1;
    }

    AVCodec *videoCodec = avcodec_find_decoder(videoStream->codecpar->codec_id);
    if (!videoCodec) {
        LOGI("Failed to find video encoder\n");
        return -1;
    }

    AVStream *outputVideoStream = avformat_new_stream(outputFormatCtx, videoCodec);
    if (!outputVideoStream) {
        LOGI("Failed to create output video stream\n");
        return -1;
    }

    if (avcodec_parameters_copy(outputVideoStream->codecpar, videoStream->codecpar) < 0) {
        LOGI("Failed to copy video codec parameters\n");
        return -1;
    }

    AVCodec *audioCodec = avcodec_find_encoder(AV_CODEC_ID_AAC);
    if (!audioCodec) {
        LOGI("Failed to find audio encoder\n");
        return -1;
    }

    AVStream *outputAudioStream = avformat_new_stream(outputFormatCtx, audioCodec);
    if (!outputAudioStream) {
        LOGI("Failed to create output audio stream\n");
        return -1;
    }

    if (avcodec_parameters_copy(outputAudioStream->codecpar, audioStream->codecpar) < 0) {
        LOGI("Failed to copy audio codec parameters\n");
        return -1;
    }

    // Open output file for writing
    if (!(outputFormatCtx->oformat->flags & AVFMT_NOFILE)) {
        if (avio_open(&outputFormatCtx->pb, outputFile, AVIO_FLAG_WRITE) < 0) {
            LOGI("Failed to open output file\n");
            return -1;
        }
    }

    if (avformat_write_header(outputFormatCtx, NULL) < 0) {
        LOGI("Failed to write output file header\n");
        return -1;
    }

    int64_t start_time = av_rescale_q(start_ms * AV_TIME_BASE, AV_TIME_BASE_Q,
                                      videoFormatCtx->streams[videoStreamIndex]->time_base);

    int64_t end_time = av_rescale_q(end_ms * AV_TIME_BASE, AV_TIME_BASE_Q,
                                    videoFormatCtx->streams[videoStreamIndex]->time_base);

    av_seek_frame(videoFormatCtx, videoStreamIndex, start_time, AVSEEK_FLAG_BACKWARD);

    AVPacket packet;
    while (av_read_frame(videoFormatCtx, &packet) >= 0) {
        if (packet.stream_index == videoStreamIndex) {
            if (packet.pts < end_time) {
                packet.pts = av_rescale_q_rnd((packet.pts - start_time), videoStream->time_base,
                                              videoStream->time_base,
                                              AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX);
                packet.dts = av_rescale_q_rnd((packet.dts - start_time), videoStream->time_base,
                                              videoStream->time_base,
                                              AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX);
                packet.duration = av_rescale_q(packet.duration, videoStream->time_base,
                                               videoStream->time_base);
                packet.pos = -1;
                if ((av_interleaved_write_frame(outputFormatCtx, &packet)) < 0) {
                    break;
                }
            }
        }
        av_packet_unref(&packet);
        if (packet.pts > end_time)
            break;
    }

    int64_t audioPts = 0;
    int64_t startTimeStream = start_ms * audioStream->time_base.den / audioStream->time_base.num;
    int64_t cutDuration = end_ms * audioStream->time_base.den / audioStream->time_base.num;
    int64_t endTimeStream = cutDuration - startTimeStream;
    av_seek_frame(audioFormatCtx, audioStreamIndex, startTimeStream, AVSEEK_FLAG_BACKWARD);
    while (av_read_frame(audioFormatCtx, &packet) >= 0) {
        if (packet.stream_index == audioStreamIndex) {
            if (audioPts + packet.duration <= endTimeStream) {
                packet.pts = audioPts;
                packet.dts = audioPts;
                audioPts += packet.duration;
                packet.stream_index = outputAudioStream->index;
                av_interleaved_write_frame(outputFormatCtx, &packet);
            }
        }
        av_packet_unref(&packet);
        if (audioPts + packet.duration > endTimeStream)
            break;
    }

    av_write_trailer(outputFormatCtx);
    avformat_close_input(&videoFormatCtx);
    avformat_close_input(&audioFormatCtx);

    if (outputFormatCtx && !(outputFormatCtx->oformat->flags & AVFMT_NOFILE))
        avio_closep(&outputFormatCtx->pb);
    avformat_free_context(outputFormatCtx);

    return 0;
}

/** */
JNIEXPORT jint JNICALL
Java_com_jc_jcsports_utils_videotrim_NDK_mp3ControlDuration(JNIEnv *env, jobject thiz,
                                                            jstring input, jstring output,
                                                            jint start_ms, jint end_ms) {


    const char *inputFileName = "/storage/emulated/0/Music/Samsung/Over_the_Horizon.mp3";
    const char *outputFileName = "/storage/emulated/0/Music/Samsung/output.mp3"; // 출력 파일 이름

    const int startTime = 60;
    const int endTime = 80;               // End time in seconds

    avformat_network_init();

    // Open input file
    AVFormatContext *inputFormatCtx = NULL;
    if (avformat_open_input(&inputFormatCtx, inputFileName, NULL, NULL) < 0) {
        LOGI("Failed to open input file\n");
        return -1;
    }

    // Retrieve input stream information
    if (avformat_find_stream_info(inputFormatCtx, NULL) < 0) {
        LOGI("Failed to retrieve input stream information\n");
        return -1;
    }

    // Find audio stream
    int audioStreamIndex = -1;
    for (int i = 0; i < inputFormatCtx->nb_streams; i++) {
        if (inputFormatCtx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            audioStreamIndex = i;
            break;
        }
    }

    if (audioStreamIndex == -1) {
        LOGI("Failed to find audio stream\n");
        return -1;
    }

    AVStream *audioStream = inputFormatCtx->streams[audioStreamIndex];

    // Calculate start and end time in stream time base units
    int64_t startTimeStream = startTime * audioStream->time_base.den / audioStream->time_base.num;
    int64_t endTimeStream = endTime * audioStream->time_base.den / audioStream->time_base.num;

    // Open output file
    AVFormatContext *outputFormatCtx = NULL;
    if (avformat_alloc_output_context2(&outputFormatCtx, NULL, NULL, outputFileName) < 0) {
        LOGI("Failed to create output format context\n");
        return -1;
    }

    // Copy audio stream from input to output
    AVStream *outputAudioStream = avformat_new_stream(outputFormatCtx, NULL);
    if (outputAudioStream == NULL) {
        LOGI("Failed to create output audio stream\n");
        return -1;
    }
    if (avcodec_parameters_copy(outputAudioStream->codecpar, audioStream->codecpar) < 0) {
        LOGI("Failed to copy codec parameters\n");
        return -1;
    }

    // Open output file for writing
    if (!(outputFormatCtx->oformat->flags & AVFMT_NOFILE)) {
        if (avio_open(&outputFormatCtx->pb, outputFileName, AVIO_FLAG_WRITE) < 0) {
            LOGI("Failed to open output file\n");
            return -1;
        }
    }

    // Write output file header
    if (avformat_write_header(outputFormatCtx, NULL) < 0) {
        LOGI("Failed to write output file header\n");
        return -1;
    }

    // Seek to start time
    if (av_seek_frame(inputFormatCtx, audioStreamIndex, startTimeStream, AVSEEK_FLAG_BACKWARD) <
        0) {
        LOGI("Failed to seek to start time\n");
        return -1;
    }

    // Read packets from input and write to output until end time is reached
    AVPacket packet;
    while (av_read_frame(inputFormatCtx, &packet) >= 0) {
        if (packet.stream_index == audioStreamIndex) {
            // Break the loop if end time is reached
            if (packet.pts >= endTimeStream)
                break;

            // Write packet to output file
            if (av_interleaved_write_frame(outputFormatCtx, &packet) < 0) {
                LOGI("Failed to write packet to output file\n");
                return -1;
            }
        }

        av_packet_unref(&packet);
    }

    // Write output file trailer
    if (av_write_trailer(outputFormatCtx) < 0) {
        LOGI("Failed to write output file trailer\n");
        return -1;
    }

    // Clean up
    avformat_close_input(&inputFormatCtx);
    avio_close(outputFormatCtx->pb);
    avformat_free_context(outputFormatCtx);
    return 0;
}

/** */
#define FILTER_DESCRIPTION "drawtext=text='Hello, World!':x=10:y=10:fontsize=24:fontcolor=red"

JNIEXPORT jint JNICALL
Java_com_jc_jcsports_utils_videotrim_NDK_test(JNIEnv *env, jobject thiz, jstring input,
                                              jstring output) {

    const char *src_path = (*env)->GetStringUTFChars(env, input, NULL);
    const char *dst_path = (*env)->GetStringUTFChars(env, output, NULL);
    AVFormatContext *input_format_ctx = NULL;
    AVFormatContext *output_format_ctx = NULL;
    AVFilterContext *buffersrc_ctx = NULL;
    AVFilterContext *buffersink_ctx = NULL;
    AVFilterGraph *filter_graph = NULL;
    AVPacket packet;
    int ret;

    const char *input_filename = src_path;
    const char *output_filename = dst_path;

    avformat_network_init();

    // Open input file
    ret = avformat_open_input(&input_format_ctx, input_filename, NULL, NULL);
    if (ret < 0) {
        LOGI( "Cannot open input file\n");
        return ret;
    }

    // Get input stream information
    ret = avformat_find_stream_info(input_format_ctx, NULL);
    if (ret < 0) {
        LOGI( "Cannot find stream information\n");
        avformat_close_input(&input_format_ctx);
        return ret;
    }

    // Open output file
    ret = avformat_alloc_output_context2(&output_format_ctx, NULL, NULL, output_filename);
    if (ret < 0) {
        LOGI( "Cannot create output context\n");
        avformat_close_input(&input_format_ctx);
        return ret;
    }

    // Copy input codec parameters to the output format
    for (int i = 0; i < input_format_ctx->nb_streams; i++) {
        AVStream *in_stream = input_format_ctx->streams[i];
        AVStream *out_stream = avformat_new_stream(output_format_ctx, in_stream->codecpar);
        if (!out_stream) {
            LOGI( "Failed allocating output stream\n");
            avformat_close_input(&input_format_ctx);
            avformat_free_context(output_format_ctx);
            return AVERROR_UNKNOWN;
        }
        ret = avcodec_parameters_copy(out_stream->codecpar, in_stream->codecpar);
        if (ret < 0) {
            LOGI( "Failed to copy codec parameters\n");
            avformat_close_input(&input_format_ctx);
            avformat_free_context(output_format_ctx);
            return ret;
        }
    }

    // Initialize the filter graph
    char args[512];
    AVFilterInOut *outputs = NULL;
    AVFilterInOut *inputs = NULL;

    filter_graph = avfilter_graph_alloc();
    if (!filter_graph) {
        LOGI( "Memory allocation error\n");
        ret = AVERROR(ENOMEM);
        goto end;
    }

    snprintf(args, sizeof(args), FILTER_DESCRIPTION);

    ret = avfilter_graph_parse_ptr(filter_graph, args, &inputs, &outputs, NULL);
    if (ret < 0) {
        LOGI("Failed to parse filter description: %s\n", av_err2str(ret));
        goto end;
    }

    ret = avfilter_graph_config(filter_graph, NULL);
    if (ret < 0) {
        LOGI( "Failed to configure filter graph\n");
        goto end;
    }

    // Open output file for writing
    ret = avio_open(&output_format_ctx->pb, output_filename, AVIO_FLAG_WRITE);
    if (ret < 0) {
        LOGI( "Cannot open output file\n");
        goto end;
    }

    // Write the stream header to the output file
    ret = avformat_write_header(output_format_ctx, NULL);
    if (ret < 0) {
        LOGI( "Error occurred when writing header to output file\n");
        goto end;
    }

    // Process frames
    while (1) {
        AVStream *in_stream;
        AVStream *out_stream;
        ret = av_read_frame(input_format_ctx, &packet);
        if (ret < 0) {
            break; // End of stream
        }

        in_stream = input_format_ctx->streams[packet.stream_index];
        out_stream = output_format_ctx->streams[packet.stream_index];

        // ... (Rest of the frame processing code, similar to previous example)

        // Send the packet to the filter graph
        ret = av_buffersrc_add_frame_flags(buffersrc_ctx, av_packet_clone(&packet), 0);
        if (ret < 0) {
            LOGI( "Error while feeding the filter graph\n");
            av_packet_unref(&packet);
            break;
        }

        // Get filtered frame from the filter graph
        AVFrame *filtered_frame = av_frame_alloc();
        if (!filtered_frame) {
            av_packet_unref(&packet);
            ret = AVERROR(ENOMEM);
            break;
        }

        ret = av_buffersink_get_frame(buffersink_ctx, filtered_frame);
        if (ret < 0) {
            if (ret == AVERROR(EAGAIN) || ret == AVERROR_EOF) {
                av_frame_free(&filtered_frame);
                av_packet_unref(&packet);
                continue;
            }
            av_frame_free(&filtered_frame);
            av_packet_unref(&packet);
            break;
        }

        // Rescale frame timestamp and duration
        filtered_frame->pts = av_rescale_q_rnd(filtered_frame->pts, buffersink_ctx->inputs[0]->time_base, out_stream->time_base, AV_ROUND_NEAR_INF|AV_ROUND_PASS_MINMAX);
        filtered_frame->pkt_dts = av_rescale_q_rnd(filtered_frame->pkt_dts, buffersink_ctx->inputs[0]->time_base, out_stream->time_base, AV_ROUND_NEAR_INF|AV_ROUND_PASS_MINMAX);
        filtered_frame->pkt_duration = av_rescale_q(filtered_frame->pkt_duration, buffersink_ctx->inputs[0]->time_base, out_stream->time_base);

        // Write the filtered frame to the output file
        ret = av_interleaved_write_frame(output_format_ctx, filtered_frame);
        av_frame_unref(filtered_frame);
        av_packet_unref(&packet);
        if (ret < 0) {
            LOGI( "Error while writing the filtered frame to the output file\n");
            break;
        }
    }

    // Write the trailer to the output file
    ret = av_write_trailer(output_format_ctx);
    if (ret < 0) {
        LOGI( "Error occurred when writing trailer to output file\n");
    }

    end:
    // Clean up
    if (inputs)
        avfilter_inout_free(&inputs);
    if (outputs)
        avfilter_inout_free(&outputs);
    if (filter_graph)
        avfilter_graph_free(&filter_graph);
    if (input_format_ctx)
        avformat_close_input(&input_format_ctx);
    if (output_format_ctx) {
        avio_closep(&output_format_ctx->pb);
        avformat_free_context(output_format_ctx);
    }

    return 0;
}
