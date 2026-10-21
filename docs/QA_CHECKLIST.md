# Manual QA checklist

Run on a real device after installing a build. Record the build (bundle version, TikTok
version, device, Android version) and mark each line **pass / fail / not tested**.
Never publish, send real messages to other people or change account settings during QA
without the account owner's consent — use a test account or the owner's explicit OK.

Build: `patches-____` · TikTok `____` · Device `____` · Android `____` · Date `____`

## A. Start-up and account

| # | Step | Expected | Result |
|---|---|---|---|
| A1 | Cold start the app | Opens without crash; does not show a video feed | |
| A2 | Sign in with email/username + password | Signed in | |
| A3 | Sign in with phone number (SMS code) | Signed in | |
| A4 | Sign in with Google | Browser-based Google sign-in completes | |
| A5 | Force-stop, reopen | Still signed in; no video feed shown | |
| A6 | Reboot phone, open app | Same as A5 | |

## B. Feeds are gone

| # | Step | Expected | Result |
|---|---|---|---|
| B1 | Look at the bottom bar | Only Create (+), Inbox, Profile | |
| B2 | Any way back to Home / For You (back button from Inbox, double-tap tabs) | No video feed | |
| B3 | Following feed | Not reachable / empty | |
| B4 | Friends feed | Not reachable / empty | |
| B5 | LIVE feed, Explore, Nearby, Shop | Not reachable | |
| B6 | Open `https://www.tiktok.com/foryou` from another app | Does not open a feed | |
| B7 | Open `https://www.tiktok.com/tag/cats` | Does not open a hashtag feed | |
| B8 | Open a sound page link | Does not open a sound feed | |
| B9 | Search for a word | Only account results | |

## C. Single video rule

| # | Step | Expected | Result |
|---|---|---|---|
| C1 | Open a video a friend sent in DM | Plays | |
| C2 | Swipe up / down on it | No other video appears | |
| C3 | Let it finish | Loops; no auto-advance | |
| C4 | No "related videos" / "more videos" panel leads to other videos | | |
| C5 | Open a video from a notification | Plays as a single video | |
| C6 | Open `https://www.tiktok.com/@user/video/<id>` from another app | Plays as a single video | |

## D. Profiles

| # | Step | Expected | Result |
|---|---|---|---|
| D1 | Own profile → tap own video | Plays, single video | |
| D2 | Friend (mutual follow) profile → tap video | Plays, single video | |
| D3 | Friend profile → Reposts / Favourites tab → tap video | Plays, single video | |
| D4 | Non-friend profile | Profile and grid visible, videos do not play | |

## E. Communication (kept)

| # | Step | Expected | Result |
|---|---|---|---|
| E1 | Inbox lists chats | | |
| E2 | Open chat, read history | | |
| E3 | Send a text message (test account or with consent) | Delivered | |
| E4 | Share a video to a friend from the video's share sheet | Sent | |
| E5 | Group chat (if the account has one) | Works | |
| E6 | Streaks / flames visible | | |
| E7 | Message notification arrives and opens the chat | | |
| E8 | Friends' stories open by tapping an avatar; no auto-jump to other authors | | |

## F. Video actions (kept)

| # | Step | Expected | Result |
|---|---|---|---|
| F1 | Like / unlike an opened video | | |
| F2 | Open and post a comment (with consent) | | |
| F3 | Add to Favourites | | |
| F4 | Repost (with consent) | | |

## G. Creator tools (kept)

| # | Step | Expected | Result |
|---|---|---|---|
| G1 | Create (+) → record with camera | Camera works | |
| G2 | Upload from gallery | | |
| G3 | Edit: trim, text, effects, music search in editor | | |
| G4 | Save as draft, reopen draft | | |
| G5 | Post as "Only me" (with consent) | Published privately | |
| G6 | Edit caption / privacy of own video; delete own test video | | |
| G7 | Analytics / creator tools page opens | | |
| G8 | Start own LIVE (if eligible, with consent) | Works | |

## H. Account management

| # | Step | Expected | Result |
|---|---|---|---|
| H1 | Settings and privacy opens; privacy toggles visible | | |
| H2 | Sign out and in again | | |
