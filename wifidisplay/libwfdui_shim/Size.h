/*
 * Copyright (C) 2023 Alcatraz323 <alcatraz32323@gmail.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#pragma once

#include <cstdint>
#include <type_traits>
#include <utility>

namespace android::ui {

/**
 * A simple value type representing a two-dimensional size
 */
struct Size {
    int32_t width;
    int32_t height;

    // Special values
    static const Size INVALID;
    static const Size EMPTY;

    // ------------------------------------------------------------------------
    // Construction
    // ------------------------------------------------------------------------

    Size() : Size(INVALID) {}
    template <typename T>
    Size(T&& w, T&& h)
          : width(w),
            height(h) {}
};
}
