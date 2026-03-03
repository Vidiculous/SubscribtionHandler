# SubHandler — Subscription Tracker

A browser-based app to track recurring subscriptions, costs, and renewal dates.

## Tech Stack

- **React 19 + TypeScript** — UI framework
- **Vite 6** — build tool (`@vitejs/plugin-react` + `@tailwindcss/vite`)
- **Tailwind CSS v4** — styling (CSS-first, no `tailwind.config.ts`)
- **date-fns v4** — all date calculations
- **localStorage** — persistence (no backend)
- **Web Notifications API** — browser notifications on app open

## Dev Commands

```bash
npm run dev       # start dev server
npm run build     # TypeScript check + Vite build
npm run preview   # preview production build
```

## Architecture

### State
- `src/context/SubscriptionContext.tsx` — central state via `useReducer` + localStorage sync
- `src/context/NotificationContext.tsx` — `Notification.permission` state + request helper
- Modal open/close state lives locally in `AppShell.tsx` (ephemeral UI state)

### Data Model
All data lives in `AppState` (persisted as JSON in localStorage under key `subscriptionHandler_v1`):
```
AppState
  subscriptions: Subscription[]    — the main list
  firedNotifications: FiredNotification[]   — dedup log for browser notifications
  schemaVersion: number            — for future migrations
```

Key fields on `Subscription`:
- `nextRenewalDate: string` — ISO `'yyyy-MM-dd'` (plain string, not Date object)
- `autoRenew: boolean` — if false, a "Mark Renewed" button is shown on the card
- `reminderDays: number` — 0 = no notification, otherwise fires X days before renewal

### Notification Strategy
Notifications fire once per billing period on every app open via `useRenewalCheck` (no service worker).
Dedup key: `${subscription.id}-${subscription.nextRenewalDate}` — changes when date advances.

### Component Tree
```
App
└── SubscriptionProvider
    └── NotificationProvider
        └── AppShell              ← modal state lives here
            ├── Header
            ├── NotificationPermissionBanner
            ├── Dashboard
            │   ├── SpendSummary
            │   ├── UpcomingRenewals  (next 7 days)
            │   └── SubscriptionGrid
            │       └── SubscriptionCard × N
            │           └── RenewButton (non-auto-renew only)
            └── Modal
                └── SubscriptionForm (add or edit)
```

## Key Files

| File | Purpose |
|---|---|
| `src/types/index.ts` | All TypeScript interfaces |
| `src/constants/index.ts` | Billing cycles, colors, storage key |
| `src/utils/dateUtils.ts` | `getDaysUntilRenewal`, `advanceByOneCycle` |
| `src/utils/costUtils.ts` | `toMonthly`, `toYearly`, `computeSpendSummary` |
| `src/utils/storageUtils.ts` | `loadState`, `saveState` |
| `src/context/SubscriptionContext.tsx` | Reducer + derived selectors |
| `src/hooks/useRenewalCheck.ts` | Browser notification logic |
| `src/components/subscription/SubscriptionForm.tsx` | Add/edit form |
| `src/components/subscription/SubscriptionCard.tsx` | Card display |

## Conventions

- Date strings are always `'yyyy-MM-dd'` format, parsed with `date-fns` `parseISO` on use
- New IDs generated with `crypto.randomUUID()`
- `schemaVersion` in persisted state — increment and add migration in `storageUtils.ts` when the `Subscription` shape changes
- Tailwind v4: use `@theme {}` in `index.css` for custom tokens, not a config file
