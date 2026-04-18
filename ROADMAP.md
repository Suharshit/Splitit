# SplitIt — Product Improvement Roadmap

A prioritized list of features to evolve SplitIt from an MVP into a production-ready portfolio product.

---

## Phase 1 — Core UX Polish (Do these first)

These make the app feel professional and fix obvious gaps in the current MVP.

- [x] **Edit existing bills** — allow users to update title, amount, or people after saving
- [x] **Undo delete** — show a Snackbar with "Undo" after swipe-to-delete instead of instant removal
- [x] **Input keyboard auto-focus** — automatically open keyboard when Add Bill screen loads
- [x] **Decimal amount formatting** — format the total amount field with commas as user types (e.g. 23,000.00)
- [x] **Empty name fallback** — if a person's name field is left blank, auto-fill as "Person N" instead of rejecting
- [x] **Bill count badge** — show total number of bills and total money tracked on the list screen header
- [x] **Scroll to top on add** — after saving a bill, scroll the list to the top to show the new entry
- [x] **Landscape layout support** — ensure all screens are usable in landscape orientation without overflow

---

## Phase 2 — Features That Make It Actually Useful

These turn it from a demo into something people would genuinely use.

- [ ] **Unequal splits** — let users assign custom amounts or percentages per person instead of equal split only
- [ ] **Mark as paid** — add a paid/unpaid toggle per person on the detail screen, with visual indicator
- [ ] **Bill categories** — tag bills with a category (Food, Travel, Rent, Shopping) with a color-coded icon
- [ ] **Currency selector** — let users pick their currency symbol (₹, $, €, £) in settings
- [ ] **Bill notes** — optional description field per bill for context (e.g. "Anniversary dinner at Taj")
- [ ] **Date stamp** — show when the bill was created; allow sorting by date
- [ ] **Search bills** — search bar on the list screen to filter by bill title or person name
- [ ] **Summary screen** — a dashboard showing total spent, number of bills, and most frequent people

---

## Phase 3 — Visual & Experience Upgrades

These make the app stand out visually in a portfolio demo.

- [ ] **Onboarding screen** — a simple 2-3 slide intro shown on first launch explaining core features
- [ ] **Animated transitions** — add shared element transitions between list → detail screen
- [ ] **Bill card redesign** — replace plain ListItem cards with richer cards showing category icon and date
- [ ] **Dark mode toggle** — explicit dark/light mode switch in settings (currently follows system)
- [ ] **Haptic feedback** — subtle vibration on swipe-to-delete and bill save confirmation
- [ ] **App shortcuts** — long-press the app icon to show "Add Bill" as a quick action shortcut
- [ ] **Splash screen** — branded splash screen using the Android 12+ SplashScreen API
- [ ] **Pull to refresh animation** — visual feedback when the list reloads (even if local)

---

## Phase 4 — Technical Improvements (Shows engineering depth)

These demonstrate architectural maturity — important for senior roles.

- [ ] **Migrate storage to Room** — replace JSON file storage with a proper Room database once a compatible KSP version is available
- [ ] **Unit tests for ViewModel** — test `addBill`, `deleteBill`, and state emissions using `kotlinx-coroutines-test`
- [ ] **UI tests with Compose Testing** — write at least one end-to-end test covering the add bill flow
- [ ] **Dependency injection with Hilt** — refactor ViewModel dependencies to use Hilt for cleaner architecture
- [ ] **Repository pattern** — add a `BillRepository` layer between ViewModel and storage to decouple data access
- [ ] **Error handling** — show user-friendly error messages if storage read/write fails
- [ ] **ProGuard rules** — configure proper ProGuard/R8 rules for the release build
- [ ] **CI with GitHub Actions** — add a workflow that builds and runs tests on every push

---

## Phase 5 — Stretch Goals (Makes it a real product)

These are ambitious but would make SplitIt genuinely publishable on the Play Store.

- [ ] **Export to PDF** — generate a clean PDF summary of a bill to share or archive
- [ ] **Recurring bills** — mark a bill as recurring (weekly/monthly) and auto-create it on schedule
- [ ] **Multiple groups** — organize bills under named groups (e.g. "Goa Trip", "Flat Expenses")
- [ ] **Contact picker integration** — pull names directly from the phone's contacts when adding people
- [ ] **Widget** — a home screen widget showing the most recent unpaid bill
- [ ] **Backup and restore** — export all bills as a JSON file to Google Drive and restore from it
- [ ] **Play Store release** — sign the APK, write a store listing, and publish to Google Play

---

## Recommended Build Order

If you want to maximize portfolio impact with minimum time investment, do these 8 features first:

1. Edit existing bills
2. Undo delete (Snackbar)
3. Mark as paid toggle
4. Bill categories with color icons
5. Summary/dashboard screen
6. Animated screen transitions
7. Migrate to Room database
8. Unit tests for ViewModel

These 8 cover UI polish, real utility, visual design, architecture, and testing — the five dimensions that Android job descriptions care about most.

---

## Current Status

| Feature | Status |
|---|---|
| Add bills with named people | Done |
| Equal split calculation | Done |
| Bill detail with per-person breakdown | Done |
| Swipe to delete | Done |
| Share bill via Android share sheet | Done |
| Custom Material3 theme | Done |
| Offline JSON persistence | Done |
| Navigation Compose | Done |

