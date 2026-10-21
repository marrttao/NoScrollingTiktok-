# Security policy

## What Unscroll does and does not do

* Adds **no** network requests, servers, analytics or telemetry.
* Logs only fixed diagnostic strings (and exception class names) to the local Android
  log. It never logs user names, ids, URLs, message text or other user content.
* Does **not** bypass login, CAPTCHA, two-step verification or other account
  protection, and does not spoof the app signature. The *Fix Google login* patch only
  makes TikTok use its own browser-based Google sign-in.
* A patched APK is signed with the build publisher's key, not TikTok's key. For a
  self-built APK, this is **your** key: keep it private and backed up. APKs from the
  project's [Telegram channel](https://t.me/SayNoToScrolling) and self-built APKs may
  use different keys. Android requires the same signing key for an in-place update.

## Risks you accept by using Unscroll

* A modified client may violate TikTok's terms of service; TikTok may restrict accounts
  that use modified clients. This has not been evaluated.
* Patched builds do not receive TikTok's security updates automatically. Update to new
  supported versions when they are listed.
* For self-patching, only use original APKs whose signature you verified as TikTok's.
  For a mod APK, review the release's source, compatibility and known limitations;
  a download link alone does not establish that a build is verified.

## Reporting a vulnerability

Please do not open a public issue for security problems. Use GitHub's
*Report a vulnerability* (private security advisory) on this repository. Include the
bundle version, TikTok version and steps to reproduce; do not include account data.
