# softmemory

### This library is my solution of &quot;Create 2 lvl cache&quot; problem on Java 8.

A complete statement is 
> Create a configurable two-level cache (for caching Objects).  
> Level 1 is memory, level 2 is filesystem. 
> Config params should let one specify the cache strategies and max sizes of level  1 and 2.

### My solution is based on 3 abstractions:
* `Memory<K, V>` - simple associative container interface;
* `Cache<K, V>` - cache replacement policies over `Memory<K, V>` buffer;
* `CachedMemory<K, V>` - extended from `Memory<K, V>` cache writing policies over `Memory<K, V>` main memory and `Cache<K, V>` buffer.

This approach makes possible to create any depth-level cache by using object of `CachedMemory` as buffer for `Cache`.
Possible 3 level case is:
1. In memory storage;
2. Filesystem storage;
3. Remote storage.

Finally, interface-based design makes library easily extensible for custom realization of replacement and writing policies.
