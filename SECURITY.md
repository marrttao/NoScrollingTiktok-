# Security policy

## What Unscroll does and does not do

* Adds **no** network requests, servers, analytics or telemetry.
* Logs only fixed diagnostic strings (and exception class names) to the local Android
  log. It never logs user names, ids, URLs, message text or other user content.
* Does **not** bypass login, CAPTCHA, two-step verification or other account
  protection, and does not spoof the app signature. The *Fix Google login* patch only
  makes TikTok use its own browser-based Google sign-in.
* Re-signs the app with **your** key. Anyone holding that keystore could ship you an
  update; keep it private and backed up.

## Risks you accept by using Unscroll

* A modified client may violate TikTok's terms of service; TikTok may restrict accounts
  that use modified clients. This has not been evaluated.
* Patched builds do not receive TikTok's security updates automatically. Update to new
  supported versions when they are listed.
* Only patch APKs whose signature you verified as TikTok's original.

## Reporting a vulnerability

Please do not open a public issue for security problems. Use GitHub's
*Report a vulnerability* (private security advisory) on this repository. Include the
bundle version, TikTok version and steps to reproduce; do not include account data.
