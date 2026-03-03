import type { AppState } from '../types';
import { SCHEMA_VERSION } from '../constants';

function defaultAppState(): AppState {
  return {
    subscriptions: [],
    firedNotifications: [],
    schemaVersion: SCHEMA_VERSION,
  };
}

function migrateState(_old: Partial<AppState>): AppState {
  // Future migrations go here
  return defaultAppState();
}

export async function loadState(): Promise<AppState> {
  try {
    const res = await fetch('/api/state');
    if (!res.ok) return defaultAppState();
    const data: AppState | null = await res.json();
    if (!data) return defaultAppState();
    if (data.schemaVersion !== SCHEMA_VERSION) return migrateState(data);
    return data;
  } catch {
    return defaultAppState();
  }
}

export async function saveState(state: AppState): Promise<void> {
  try {
    await fetch('/api/state', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(state),
    });
  } catch {
    // Server unavailable — silently fail
  }
}
