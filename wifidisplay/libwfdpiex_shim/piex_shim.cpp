/*
 * Copyright (C) 2023 Alcatraz323 <alcatraz32323@gmail.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#include <src/piex.h>

namespace piex {

Error GetPreviewImageData(StreamInterface* data, PreviewImageData* preview_image_data) {
    return GetPreviewImageData(data, preview_image_data, nullptr);
}

}  // namespace piex
