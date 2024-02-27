/*
 * Copyright (C) 2023 Alcatraz323 <alcatraz32323@gmail.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#include <input/Input.h>

// libinput 32-bit
extern "C" void _ZN7android11MotionEvent10initializeEiijiNSt3__15arrayIhLj32EEEiiiiiiNS_20MotionClassificationERKNS_2ui9TransformEffffS8_xxjPKNS_17PointerPropertiesEPKNS_13PointerCoordsE(
                             int32_t id, int32_t deviceId, uint32_t source, int32_t displayId,
                             std::array<uint8_t, 32> hmac, int32_t action, int32_t actionButton,
                             int32_t flags, int32_t edgeFlags, int32_t metaState,
                             int32_t buttonState, android::MotionClassification classification,
                             const android::ui::Transform& transform, float xPrecision, float yPrecision,
                             float rawXCursorPosition, float rawYCursorPosition,
                             const android::ui::Transform& rawTransform, nsecs_t downTime, nsecs_t eventTime,
                             size_t pointerCount, const android::PointerProperties* pointerProperties,
                             const android::PointerCoords* pointerCoords);

// libwfdnative 32-bit
extern "C" void _ZN7android11MotionEvent10initializeEiijiNSt3__15arrayIhLj32EEEiiiiiiNS_20MotionClassificationERKNS_2ui9TransformEffffjiixxjPKNS_17PointerPropertiesEPKNS_13PointerCoordsE(
                             int32_t id, int32_t deviceId, uint32_t source, int32_t displayId,
                             std::array<uint8_t, 32> hmac, int32_t action, int32_t actionButton,
                             int32_t flags, int32_t edgeFlags, int32_t metaState,
                             int32_t buttonState, android::MotionClassification classification,
                             const android::ui::Transform& transform, float xPrecision, float yPrecision,
                             float rawXCursorPosition, float rawYCursorPosition,
                             uint32_t displayOrientation, int32_t displayWidth,
                             int32_t displayHeight, nsecs_t downTime, nsecs_t eventTime,
                             size_t pointerCount, const android::PointerProperties* pointerProperties,
                             const android::PointerCoords* pointerCoords) {
    android::ui::Transform identityTransform;
    _ZN7android11MotionEvent10initializeEiijiNSt3__15arrayIhLj32EEEiiiiiiNS_20MotionClassificationERKNS_2ui9TransformEffffS8_xxjPKNS_17PointerPropertiesEPKNS_13PointerCoordsE(
                            id, deviceId, source, displayId, hmac, action, actionButton, flags, edgeFlags, metaState, buttonState,
                            classification, transform, xPrecision, yPrecision, rawXCursorPosition, rawYCursorPosition, identityTransform,
                            downTime, eventTime, pointerCount, pointerProperties, pointerCoords
    );
}
