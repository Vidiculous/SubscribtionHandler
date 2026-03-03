import { createContext, useContext, useReducer, useEffect, useRef, useMemo, type ReactNode } from 'react';
import type { AppState, SubscriptionAction, SpendSummary, UpcomingRenewal } from '../types';
import { loadState, saveState } from '../utils/storageUtils';
import { computeSpendSummary } from '../utils/costUtils';
import { getDaysUntilRenewal, advanceByOneCycle } from '../utils/dateUtils';

interface SubscriptionContextValue {
  state: AppState;
  dispatch: React.Dispatch<SubscriptionAction>;
  spendSummary: SpendSummary;
  upcomingRenewals: UpcomingRenewal[];
}

function subscriptionReducer(state: AppState, action: SubscriptionAction): AppState {
  const now = new Date().toISOString();

  switch (action.type) {
    case 'HYDRATE':
      return action.payload;

    case 'ADD_SUBSCRIPTION':
      return { ...state, subscriptions: [...state.subscriptions, action.payload] };

    case 'UPDATE_SUBSCRIPTION':
      return {
        ...state,
        subscriptions: state.subscriptions.map((s) =>
          s.id === action.payload.id ? action.payload : s
        ),
      };

    case 'DELETE_SUBSCRIPTION':
      return {
        ...state,
        subscriptions: state.subscriptions.filter((s) => s.id !== action.payload.id),
      };

    case 'RENEW_SUBSCRIPTION': {
      const updated = state.subscriptions.map((s) => {
        if (s.id !== action.payload.id) return s;
        return {
          ...s,
          nextRenewalDate: advanceByOneCycle(s),
          updatedAt: now,
        };
      });
      return { ...state, subscriptions: updated };
    }

    case 'MARK_NOTIFICATION_FIRED':
      return {
        ...state,
        firedNotifications: [...state.firedNotifications, action.payload],
      };

    default:
      return state;
  }
}

const SubscriptionContext = createContext<SubscriptionContextValue | null>(null);

export function SubscriptionProvider({ children }: { children: ReactNode }) {
  const [state, dispatch] = useReducer(subscriptionReducer, {
    subscriptions: [],
    firedNotifications: [],
    schemaVersion: 1,
  });

  const isHydratedRef = useRef(false);

  // Async hydration from server
  useEffect(() => {
    loadState().then((saved) => {
      dispatch({ type: 'HYDRATE', payload: saved });
      isHydratedRef.current = true;
    });
  }, []);

  // Persist on every state change (skip initial empty state before hydration)
  useEffect(() => {
    if (!isHydratedRef.current) return;
    saveState(state);
  }, [state]);

  const spendSummary = useMemo(
    () => computeSpendSummary(state.subscriptions),
    [state.subscriptions]
  );

  const upcomingRenewals = useMemo<UpcomingRenewal[]>(() => {
    return state.subscriptions
      .map((sub) => ({ subscription: sub, daysUntil: getDaysUntilRenewal(sub.nextRenewalDate) }))
      .filter((r) => r.daysUntil >= 0 && r.daysUntil <= 7)
      .sort((a, b) => a.daysUntil - b.daysUntil);
  }, [state.subscriptions]);

  return (
    <SubscriptionContext.Provider value={{ state, dispatch, spendSummary, upcomingRenewals }}>
      {children}
    </SubscriptionContext.Provider>
  );
}

export function useSubscriptions(): SubscriptionContextValue {
  const ctx = useContext(SubscriptionContext);
  if (!ctx) throw new Error('useSubscriptions must be used within SubscriptionProvider');
  return ctx;
}
