/*
 * Copyright (C) 2023 Alcatraz323 <alcatraz32323@gmail.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#include <gui/SurfaceComposerClient.h>

extern "C" void _ZN7android21SurfaceComposerClient11Transaction5applyEbb(bool synchronous,
                                                                         bool oneWay);

extern "C" void _ZN7android21SurfaceComposerClient11Transaction5applyEb(bool synchronous) {
    _ZN7android21SurfaceComposerClient11Transaction5applyEbb(synchronous, false);
}

extern "C" void _ZN7android21SurfaceComposerClient11Transaction20setDisplayLayerStackERKNS_2spINS_7IBinderEEENS_2ui10LayerStackE(const android::sp<android::IBinder>& token,
                                                              android::ui::LayerStack layerStack);

extern "C" void _ZN7android21SurfaceComposerClient11Transaction20setDisplayLayerStackERKNS_2spINS_7IBinderEEEj(const android::sp<android::IBinder>& token,
        uint32_t layerStack) {
    _ZN7android21SurfaceComposerClient11Transaction20setDisplayLayerStackERKNS_2spINS_7IBinderEEENS_2ui10LayerStackE(token, android::ui::LayerStack::fromValue(layerStack));
}
