# brinvex-java review findings

2026-11-06

brinvex-java 1.40.13 (Java 21) has 7 confirmed bugs, 6 smaller issues, 12 helpers the JDK already provides, and 6 design clean-ups. Every bug below was reproduced with a probe run against `target/classes`.

Work through the list top to bottom, one item at a time, and tick each box when it is fixed, tested and committed. Bugs come first because they change behaviour. JDK replacements and design changes may break callers in other brinvex projects, so treat them as API changes.

## Confirmed bugs

Each item names the file, what goes wrong, what the probe printed, and the likely fix.

- [x] **1. `Num.setScale1` rounds to 2 decimals.** `Num.java`: it calls `setScale(bd, 2, …)`. Probe: `setScale1(1.234)` returned `1.23`. Fix: use scale 1 and add a test.
- [ ] **2. `Collectors.toHashMap` declares the wrong return type.** `collection/Collectors.java`: the signature says `LinkedHashMap` but the supplier is `HashMap::new`. Probe: assigning the result to a `LinkedHashMap` threw `ClassCastException`. Fix: return `Collector<T, ?, HashMap<K, U>>`.
- [ ] **3. `Collectors.toLinkedSet` loses order in parallel streams.** It is flagged `UNORDERED`, and its combiner may add left into right. Probe: 1..20 in parallel came back as `[4, 5, 3, 1, 2, …]`. Fix: use only `IDENTITY_FINISH` and always do `left.addAll(right)`.
- [ ] **4. `CollectionPrintUtil` drops equal rows.** Rows are stored in a `Map<E, List<String>>` keyed by the item, so equal items overwrite each other. Probe: 3 records printed as 2 rows. Fix: store rows in a `List<List<String>>`.
- [ ] **5. `Check.isTrue` / `isFalse` accept `null`.** `validation/Check.java`: a `null` Boolean returns no violation, so `Assert.isTrue(null)` and `Validate.isTrue(null)` pass silently. Fix: treat `null` as a violation, or take a primitive `boolean`.
    - Open question: should a `null` Boolean count as a violation, or should `isTrue`/`isFalse` take a primitive `boolean`?
- [ ] **6. `UriUtil.extractDomainName` strips too much.** The regex `^[wW]{3}.*?\.` also matches hosts that only start with "www". Probe: `https://wwwexample.com/x` returned `com`. Fix: use `URI.create(url).getHost()` and remove a leading `www.` only.
- [ ] **7. `StringUtil.stripToNull` / `stripToEmpty` only trim ASCII.** They call `trim()`, so Unicode spaces stay. Probe: an em-space around `x` was not removed. Fix: use `strip()`, which matches the method names.

## Smaller issues

These are not reproduced as failures, but each one is a trap or leftover code.

- [ ] **8. `StringUtil.Constants` exposes mutable arrays.** Any caller can change `DIGITS_CHARS[0]` for everyone. Fix: expose `String` constants, or return copies.
- [ ] **9. `HttpUtil.appendCookies` sends one `Cookie` header per cookie.** RFC 6265 expects a single header with values joined by `"; "`. Fix: join the cookies into one header.
- [ ] **10. `HttpUtil.detectCharset` fails on quoted values.** `Content-Type: text/html; charset="utf-8"` makes `Charset.forName` throw. Fix: strip quotes and spaces first.
- [ ] **11. `ThreadUtil` has unused imports.** About 8 imports are left over from the removed concurrency code. Fix: delete them.
- [ ] **12. `IOCallUtil.uncheckedIO(NoArgIOCall)` has an unused type parameter `<I>`.** Fix: remove it.
- [ ] **13. `FtpUtil.fetch` uses the deprecated `new URL(String)`.** It is deprecated since Java 20, and the method works for any URL, not just FTP. Fix: use `URI.create(url).toURL()` and consider a more general name.

## Already built into Java 21

These 12 helpers duplicate JDK methods. Replacing them removes code, but callers in other brinvex projects must migrate. Fill in a decision per row: Replace with JDK / Keep / Done.

| # | Library code | JDK 21 equivalent | Caveat | Decision |
| --- | --- | --- | --- | --- |
| 14 | `LambdaUtil.chain(f1, f2)` | `f1.andThen(f2)` | Predicate variants need `p::test` | |
| 15 | `Num.min(a, b)` / `Num.max(a, b)` | `a.min(b)` / `a.max(b)` | None | |
| 16 | `Num.isZero`, `isPositive`, `isNegative`… | `bd.signum() == 0` / `> 0` / `< 0` | Yours read better; keep is reasonable | |
| 17 | `DateUtil.bothNullOrEqual` | `Objects.equals(a, b)` | Same result for ISO dates | |
| 18 | `DigestUtil.md5` hex loop | `HexFormat.of().formatHex(bytes)` | Keep the method, replace the loop | |
| 19 | `NullUtil.coalesce(a, supplier)` | `Objects.requireNonNullElseGet(a, supplier)` | JDK throws if the fallback is null | |
| 20 | `NullUtil.nullSafe(x, f1, f2…)` | `Optional.ofNullable(x).map(f1).map(f2).orElse(null)` | Optional allocates per step | |
| 21 | `Collectors.toHashSet` / `toLinkedSet` | `Collectors.toCollection(HashSet::new)` / `(LinkedHashSet::new)` | Also fixes bug 3 | |
| 22 | `CollectionUtil.getLast(list)` | `list.getLast()` | JDK throws on an empty list; yours returns null | |
| 23 | `UriUtil.extractSchemeAndHost` / `extractDomainName` | `URI.create(url).getHost()` | Also fixes bug 6 | |
| 24 | `HttpUtil` cookie helpers | `HttpClient.newBuilder().cookieHandler(new CookieManager())` | Also fixes issue 9 | |
| 25 | `CollectionUtil.copyToLinkedMap` | `new LinkedHashMap<>(map)` | Already `@Deprecated`; delete | |

Newer JDKs add two more overlaps. `ChunkUtil` can become `Gatherers.windowFixed(n)` once you move past Java 24. The JDK's own `java.lang.LazyConstant` (still in preview) clashes with yours by name, see item 29.

## Design suggestions

These change the public API, so batch them into one release with a clear changelog.

- [ ] **26. Trim shortcut overloads.** `toChunks500`/`toChunks1000` (6 methods), `Num.setScale0..8`, `multiplyP8/12/16` and 6 arities of `linkedMap` add surface but little value. Keep only the ones other projects really call.
- [ ] **27. Merge `Assert` and `Validate`.** They are about 150 identical lines each; only the exception type differs. One implementation with an exception factory removes the copy.
- [ ] **28. Move domain-specific code out.** `CollectionUtil.sumConsecutiveEntries` (`LocalDate → BigDecimal` with randomness), `StringUtil.splitByPercentile`, `StringUtil.generateWords` and `RandomUtil` fit the project that uses them better than a common library.
- [ ] **29. Rename `LazyConstant`.** If `java.lang.LazyConstant` becomes final, `import com.brinvex.java.*` gives an "ambiguous name" compile error.
- [ ] **30. Rename `com.brinvex.java.collection.Collectors`.** It shares its name with `java.util.stream.Collectors`, and `HttpUtil` already imports the JDK one. `MoreCollectors` or `BxCollectors` avoids the clash.
- [ ] **31. Turn `DateRange` into a `record`.** The cached `startExcl`/`endIncl` fields make it mutable to save one `minusDays(1)` call. A record with computed accessors is simpler and immutable.
