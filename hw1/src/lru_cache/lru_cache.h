/***
 * Author: Tsutsiev A.S., M34361
 */

// ReSharper disable CommentTypo

#pragma once

#include <unordered_map>
#include <optional>
#include <cassert>

namespace lru_cache
{
  template <class KeyType, class ValueType>
  class lru_cache final
  {
  public:
    struct node
    {
      const KeyType key;
      const ValueType value;
      node(const KeyType key, const ValueType value) : key(key), value(value) {}
      bool operator==(const node & other) { return key == other.key && value == other.value; }
    };

  private:
    const size_t capacity;
    std::list<node> keys_cache;
    std::unordered_map<KeyType, node> key_refs;

  public:
    explicit lru_cache(const size_t capacity) : capacity(capacity) {}

    void put(const KeyType& key, const ValueType& value)
    {
      remove_node(key);
      add_node(key, value);
      assert(!keys_cache.empty());
      assert(keys_cache.front().value == value);
      assert(size() <= capacity);
    }

    lru_cache& operator<<(const std::pair<KeyType, ValueType> & key_value) { put(key_value.first, key_value.second); return *this; }
    std::optional<ValueType> operator[](const KeyType& key) { return get(key); }

    std::optional<ValueType> get(const KeyType& key)
    {
      const size_t size_before = size();
      std::optional<ValueType> res = {};

      const auto value_it = key_refs.find(key);
      if (value_it != key_refs.end())
      {
        const ValueType value = value_it->second.value;
        remove_node(key);
        add_node(key, value);
        res = { value };
        assert(keys_cache.front().value == res.value());
      }
      assert(size_before == size());
      return res;
    }

    [[nodiscard]] size_t size() const { return key_refs.size(); }

  private:
    std::optional<ValueType> inner_get(const KeyType& key)
    {
      const auto value_it = key_refs.find(key);

      if (value_it == key_refs.end()) return {};

      const ValueType value = value_it->second.value;
      remove_node(key);
      add_node(key, value);

      return { value };
    }

    void remove_node(const KeyType& key)
    {
      const auto cur_node_refs_it = key_refs.find(key);

      if (cur_node_refs_it == key_refs.end()) return;

      const auto cur_node = cur_node_refs_it->second;

      const auto cur_node_cache_it = std::find(keys_cache.begin(), keys_cache.end(), cur_node);

      assert(cur_node_cache_it != keys_cache.end());
      keys_cache.erase(cur_node_cache_it);
      key_refs.erase(cur_node_refs_it);
    }

    void add_node(const KeyType& key, const ValueType& value)
    {
      node new_node = node(key, value);

      keys_cache.push_front(new_node);

      key_refs.insert({ key, new_node });
      if (size() > capacity) remove_node(keys_cache.back().key);
    }
  };
}
