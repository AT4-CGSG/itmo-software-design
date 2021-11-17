/***
 * Author: Tsutsiev A.S., M34361
 */

//#pragma once

#define GTEST_LANG_CXX11 1
#include <gtest/gtest.h>

#include <cassert>

#include "../../src/lru_cache/lru_cache.h"

namespace lru_cache_test
{
  TEST(lru_cache_test, test_empty)
  {
    auto cache = lru_cache::lru_cache<int, int>(1);
    EXPECT_FALSE(cache.get(0).has_value());
  }

  TEST(lru_cache_test, test_put_and_get)
  {
    auto cache = lru_cache::lru_cache<int, int>(1);
    cache.put(0, 0);

    const auto get0 = cache.get(0);
    EXPECT_TRUE(get0.has_value());
    EXPECT_EQ(get0.value(), 0);
  }

  TEST(lru_cache_test, test_multiple_put)
  {
    auto cache = lru_cache::lru_cache<int, int>(2);
    cache << std::make_pair(1, 1);
    EXPECT_EQ(cache.size(), 1);
    cache << std::make_pair(2, 2);
    EXPECT_EQ(cache.size(), 2);
    cache << std::make_pair(1, 1);
    EXPECT_EQ(cache.size(), 2);
    cache << std::make_pair(3, 3);
    EXPECT_EQ(cache.size(), 2);

    const auto get1 = cache[1];
    EXPECT_TRUE(get1.has_value());
    EXPECT_EQ(get1.value(), 1);
  }
}
