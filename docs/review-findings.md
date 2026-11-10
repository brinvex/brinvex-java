# brinvex-java review findings

2026-11-06

brinvex-java 1.40.13 (Java 21) has 6 confirmed bugs (item 5 turned out to be intended behaviour), 6 smaller issues, 12 helpers the JDK already provides, and 6 design clean-ups. Every bug below was reproduced with a probe run against `target/classes`.

Work through the list top to bottom, one item at a time, and update the Status column as you go. Status values: **Not decided yet**, **Ready to fix**, **Done**
(fixed, tested and committed), **Keep** (decided to leave the code as it is). Bugs come first because they change behaviour. JDK replacements and design changes
may break callers in other brinvex projects, so treat them as API changes.

## Confirmed bugs

Each row names the file, what goes wrong, what the probe printed, and the fix.

| # | Finding                                                   | Details and fix                                                                                                                                                                                                                                                        | Status       |
|---|-----------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|
| 1 | `Num.setScale1` rounds to 2 decimals                      | `Num.java` calls `setScale(bd, 2, …)`. Probe: `setScale1(1.234)` returned `1.23`. Fixed to scale 1, covered by `NumTest`.                                                                                                                                              | Done         |
| 2 | `Collectors.toHashMap` declares the wrong return type     | `collection/Collectors.java`: the signature says `LinkedHashMap` but the supplier is `HashMap::new`. Probe: assigning the result to a `LinkedHashMap` threw `ClassCastException`. Now returns `Collector<T, ?, HashMap<K, U>>`, covered by `CollectorTest`.               | Done         |
| 3 | `Collectors.toLinkedSet` loses order in parallel streams  | It is flagged `UNORDERED`, and its combiner may add left into right. Probe: 1..20 in parallel came back as `[4, 5, 3, 1, 2, …]`. Now uses only `IDENTITY_FINISH` and always does `left.addAll(right)`, covered by `CollectorTest`.                                        | Done         |
| 4 | `CollectionPrintUtil` drops equal rows                    | Rows are stored in a `Map<E, List<String>>` keyed by the item, so equal items overwrite each other. Probe: 3 records printed as 2 rows. Rows are now stored in a `List<List<String>>`, covered by `CollectionPrintUtilTest`.                                           | Done         |
| 5 | `Check.isTrue` / `isFalse` accept `null`                  | Not a bug: every check except `notNull` and `notNullNotBlank` lets `null` pass, the same convention as Jakarta Bean Validation (`@AssertTrue`, `@Positive`, `@Pattern`). Optional follow-up: state this rule in the class Javadoc of `Check`, `Assert` and `Validate`. | Keep         |
| 6 | `UriUtil.extractDomainName` strips too much               | The regex `^[wW]{3}.*?\.` also matches hosts that only start with "www". Probe: `https://wwwexample.com/x` returned `com`. The regex now strips a "www…" label only when at least two labels follow, so `www2.`/`wwwABC.` prefixes are still removed; covered by `UriUtilTest`. | Done         |
| 7 | `StringUtil.stripToNull` / `stripToEmpty` only trim ASCII | They call `trim()`, so Unicode spaces stay. Probe: an em-space around `x` was not removed. Now uses `strip()`, which matches the method names; covered by `StringUtilTest`.                                                                                            | Done         |

## Smaller issues

These are not reproduced as failures, but each one is a trap or leftover code.

| #  | Finding                                                                  | Details and fix                                                                                                                                                                         | Status       |
|----|--------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|
| 8  | `StringUtil.Constants` exposes mutable arrays                            | Any caller can change `DIGITS_CHARS[0]` for everyone. Made private; the test that used it was removed. API change: check other brinvex projects for `StringUtil.Constants` before releasing. | Done         |
| 9  | `HttpUtil.appendCookies` sends one `Cookie` header per cookie            | RFC 6265 expects a single header with values joined by `"; "`. Cookies are now joined into one header, with no header for an empty collection; covered by `HttpUtilTest.appendCookies`. | Done         |
| 10 | `HttpUtil.detectCharset` fails on quoted values                          | `Content-Type: text/html; charset="utf-8"` makes `Charset.forName` throw. Spaces and surrounding quotes are now stripped first; covered by `HttpUtilTest`.                               | Done         |
| 11 | `ThreadUtil` has unused imports                                          | About 8 imports were left over from the removed concurrency code. Removed.                                                                                                              | Done         |
| 12 | `IOCallUtil.uncheckedIO(NoArgIOCall)` has an unused type parameter `<I>` | Removed; the no-arg overload is now covered by `IOCallUtilTest`.                                                                                                                       | Done         |
| 13 | `FtpUtil.fetch` uses the deprecated `new URL(String)`                    | It is deprecated since Java 20, and the method works for any URL, not just FTP. Fix: use `URI.create(url).toURL()` and consider a more general name.                                    | Ready to fix |

## Already built into Java 21

These 12 helpers duplicate JDK methods. Replacing them removes code, but callers in other brinvex projects must migrate.

| #  | Library code                                         | JDK 21 equivalent                                                | Caveat                                                     | Status          |
|----|------------------------------------------------------|------------------------------------------------------------------|------------------------------------------------------------|-----------------|
| 14 | `LambdaUtil.chain(f1, f2)`                           | `f1.andThen(f2)`                                                 | Predicate variants need `p::test`                          | Not decided yet |
| 15 | `Num.min(a, b)` / `Num.max(a, b)`                    | `a.min(b)` / `a.max(b)`                                          | None                                                       | Not decided yet |
| 16 | `Num.isZero`, `isPositive`, `isNegative`…            | `bd.signum() == 0` / `> 0` / `< 0`                               | Yours read better; keeping them is reasonable              | Not decided yet |
| 17 | `DateUtil.bothNullOrEqual`                           | `Objects.equals(a, b)`                                           | Same result for ISO dates                                  | Not decided yet |
| 18 | `DigestUtil.md5` hex loop                            | `HexFormat.of().formatHex(bytes)`                                | Keep the method, replace the loop                          | Not decided yet |
| 19 | `NullUtil.coalesce(a, supplier)`                     | `Objects.requireNonNullElseGet(a, supplier)`                     | The JDK method throws if the fallback is null              | Not decided yet |
| 20 | `NullUtil.nullSafe(x, f1, f2…)`                      | `Optional.ofNullable(x).map(f1).map(f2).orElse(null)`            | Optional allocates per step                                | Not decided yet |
| 21 | `Collectors.toHashSet` / `toLinkedSet`               | `Collectors.toCollection(HashSet::new)` / `(LinkedHashSet::new)` | Kept, so bug 3 must be fixed in `toLinkedSet` itself       | Keep            |
| 22 | `CollectionUtil.getLast(list)`                       | `list.getLast()`                                                 | The JDK method throws on an empty list; yours returns null | Not decided yet |
| 23 | `UriUtil.extractSchemeAndHost` / `extractDomainName` | `URI.create(url).getHost()`                                      | Returns `null` for URLs without a scheme (`brinvex.com/x`) | Not decided yet |
| 24 | `HttpUtil` cookie helpers                            | `HttpClient.newBuilder().cookieHandler(new CookieManager())`     | Would replace the item 9 fix                               | Not decided yet |
| 25 | `CollectionUtil.copyToLinkedMap`                     | `new LinkedHashMap<>(map)`                                       | Already `@Deprecated`; delete it                           | Not decided yet |

Newer JDKs add two more overlaps. `ChunkUtil` can become `Gatherers.windowFixed(n)` once you move past Java 24. The JDK's own `java.lang.LazyConstant` (still in preview) clashes with yours by name, see item 29.

## Design suggestions

These change the public API, so batch them into one release with a clear changelog.

| #  | Suggestion                                      | Details                                                                                                                                                                                                                     | Status          |
|----|-------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------|
| 26 | Trim shortcut overloads                         | `toChunks500`/`toChunks1000` (6 methods), `Num.setScale0..8`, `multiplyP8/12/16` and 6 arities of `linkedMap` add surface but little value. Keep only the ones other projects really call.                                  | Not decided yet |
| 27 | Merge `Assert` and `Validate`                   | They are about 150 identical lines each; only the exception type differs. One implementation with an exception factory removes the copy.                                                                                    | Not decided yet |
| 28 | Move domain-specific code out                   | `CollectionUtil.sumConsecutiveEntries` (`LocalDate → BigDecimal` with randomness), `StringUtil.splitByPercentile`, `StringUtil.generateWords` and `RandomUtil` fit the project that uses them better than a common library. | Not decided yet |
| 29 | Rename `LazyConstant`                           | If `java.lang.LazyConstant` becomes final, `import com.brinvex.java.*` gives an "ambiguous name" compile error.                                                                                                             | Not decided yet |
| 30 | Rename `com.brinvex.java.collection.Collectors` | It shares its name with `java.util.stream.Collectors`, and `HttpUtil` already imports the JDK one. `MoreCollectors` or `BxCollectors` avoids the clash.                                                                     | Not decided yet |
| 31 | Turn `DateRange` into a `record`                | The cached `startExcl`/`endIncl` fields make it mutable to save one `minusDays(1)` call. A record with computed accessors is simpler and immutable.                                                                         | Not decided yet |
