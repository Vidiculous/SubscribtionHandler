import { useState, type FormEvent } from 'react';
import { format } from 'date-fns';
import type { Subscription, BillingCycle, CustomCycleUnit } from '../../types';
import { useSubscriptions } from '../../context/SubscriptionContext';
import { Button } from '../ui/Button';
import {
  BILLING_CYCLES,
  BILLING_CYCLE_LABELS,
  CUSTOM_CYCLE_UNITS,
  DEFAULT_REMINDER_DAYS,
  PRESET_COLORS,
  COMMON_CURRENCIES,
} from '../../constants';

interface SubscriptionFormProps {
  initial?: Subscription;
  onDone: () => void;
}

function todayStr() {
  return format(new Date(), 'yyyy-MM-dd');
}

function nextMonthStr() {
  const d = new Date();
  d.setMonth(d.getMonth() + 1);
  return format(d, 'yyyy-MM-dd');
}

export function SubscriptionForm({ initial, onDone }: SubscriptionFormProps) {
  const { dispatch } = useSubscriptions();
  const isEdit = !!initial;

  const [name, setName] = useState(initial?.name ?? '');
  const [cost, setCost] = useState(initial?.cost?.toString() ?? '');
  const [currency, setCurrency] = useState(initial?.currency ?? 'USD');
  const [billingCycle, setBillingCycle] = useState<BillingCycle>(initial?.billingCycle ?? 'monthly');
  const [customCycleAmount, setCustomCycleAmount] = useState(
    initial?.customCycleAmount?.toString() ?? '1'
  );
  const [customCycleUnit, setCustomCycleUnit] = useState<CustomCycleUnit>(
    initial?.customCycleUnit ?? 'months'
  );
  const [nextRenewalDate, setNextRenewalDate] = useState(
    initial?.nextRenewalDate ?? nextMonthStr()
  );
  const [autoRenew, setAutoRenew] = useState(initial?.autoRenew ?? true);
  const [reminderDays, setReminderDays] = useState(
    initial?.reminderDays?.toString() ?? DEFAULT_REMINDER_DAYS.toString()
  );
  const [color, setColor] = useState(initial?.color ?? PRESET_COLORS[0]);
  const [notes, setNotes] = useState(initial?.notes ?? '');
  const [errors, setErrors] = useState<Record<string, string>>({});

  function validate(): boolean {
    const errs: Record<string, string> = {};
    if (!name.trim()) errs.name = 'Name is required';
    const costNum = parseFloat(cost);
    if (isNaN(costNum) || costNum < 0) errs.cost = 'Enter a valid cost (0 or more)';
    if (!nextRenewalDate) errs.nextRenewalDate = 'Renewal date is required';
    if (billingCycle === 'custom') {
      const amt = parseInt(customCycleAmount);
      if (isNaN(amt) || amt < 1) errs.customCycleAmount = 'Enter a number ≥ 1';
    }
    const remDays = parseInt(reminderDays);
    if (isNaN(remDays) || remDays < 0) errs.reminderDays = 'Enter 0 or more';
    setErrors(errs);
    return Object.keys(errs).length === 0;
  }

  function handleSubmit(e: FormEvent) {
    e.preventDefault();
    if (!validate()) return;

    const now = new Date().toISOString();
    const sub: Subscription = {
      id: initial?.id ?? crypto.randomUUID(),
      name: name.trim(),
      cost: parseFloat(cost),
      currency,
      billingCycle,
      ...(billingCycle === 'custom' && {
        customCycleAmount: parseInt(customCycleAmount),
        customCycleUnit,
      }),
      nextRenewalDate,
      autoRenew,
      reminderDays: parseInt(reminderDays),
      color,
      notes: notes.trim() || undefined,
      createdAt: initial?.createdAt ?? now,
      updatedAt: now,
    };

    if (isEdit) {
      dispatch({ type: 'UPDATE_SUBSCRIPTION', payload: sub });
    } else {
      dispatch({ type: 'ADD_SUBSCRIPTION', payload: sub });
    }
    onDone();
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-5" noValidate>
      {/* Name */}
      <div>
        <label className="block text-sm font-medium text-slate-300 mb-1.5">
          Name <span className="text-red-500">*</span>
        </label>
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="e.g. Netflix, Spotify"
          className={`w-full rounded-lg border px-3.5 py-2.5 text-sm text-slate-100
            bg-slate-800 placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-indigo-500
            ${errors.name ? 'border-red-500' : 'border-slate-600'}`}
          autoFocus
        />
        {errors.name && <p className="mt-1 text-xs text-red-500">{errors.name}</p>}
      </div>

      {/* Cost + Currency */}
      <div className="grid grid-cols-2 gap-3">
        <div>
          <label className="block text-sm font-medium text-slate-300 mb-1.5">
            Cost <span className="text-red-500">*</span>
          </label>
          <input
            type="number"
            value={cost}
            onChange={(e) => setCost(e.target.value)}
            placeholder="0.00"
            min="0"
            step="0.01"
            className={`w-full rounded-lg border px-3.5 py-2.5 text-sm text-slate-100
              bg-slate-800 placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-indigo-500
              ${errors.cost ? 'border-red-500' : 'border-slate-600'}`}
          />
          {errors.cost && <p className="mt-1 text-xs text-red-500">{errors.cost}</p>}
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-300 mb-1.5">Currency</label>
          <select
            value={currency}
            onChange={(e) => setCurrency(e.target.value)}
            className="w-full rounded-lg border border-slate-600 px-3.5 py-2.5 text-sm
              text-slate-100 bg-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            {COMMON_CURRENCIES.map((c) => (
              <option key={c} value={c}>
                {c}
              </option>
            ))}
          </select>
        </div>
      </div>

      {/* Billing Cycle */}
      <div>
        <label className="block text-sm font-medium text-slate-300 mb-1.5">Billing Cycle</label>
        <select
          value={billingCycle}
          onChange={(e) => setBillingCycle(e.target.value as BillingCycle)}
          className="w-full rounded-lg border border-slate-600 px-3.5 py-2.5 text-sm
            text-slate-100 bg-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500"
        >
          {BILLING_CYCLES.map((cycle) => (
            <option key={cycle} value={cycle}>
              {BILLING_CYCLE_LABELS[cycle]}
            </option>
          ))}
        </select>
      </div>

      {/* Custom Cycle Fields */}
      {billingCycle === 'custom' && (
        <div>
          <label className="block text-sm font-medium text-slate-300 mb-1.5">Every</label>
          <div className="flex gap-3">
            <input
              type="number"
              value={customCycleAmount}
              onChange={(e) => setCustomCycleAmount(e.target.value)}
              min="1"
              className={`w-24 rounded-lg border px-3.5 py-2.5 text-sm text-slate-100
                bg-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500
                ${errors.customCycleAmount ? 'border-red-500' : 'border-slate-600'}`}
            />
            <select
              value={customCycleUnit}
              onChange={(e) => setCustomCycleUnit(e.target.value as CustomCycleUnit)}
              className="flex-1 rounded-lg border border-slate-600 px-3.5 py-2.5 text-sm
                text-slate-100 bg-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500"
            >
              {CUSTOM_CYCLE_UNITS.map((u) => (
                <option key={u} value={u}>
                  {u.charAt(0).toUpperCase() + u.slice(1)}
                </option>
              ))}
            </select>
          </div>
          {errors.customCycleAmount && (
            <p className="mt-1 text-xs text-red-500">{errors.customCycleAmount}</p>
          )}
        </div>
      )}

      {/* Next Renewal Date */}
      <div>
        <label className="block text-sm font-medium text-slate-300 mb-1.5">
          Next Renewal Date <span className="text-red-500">*</span>
        </label>
        <input
          type="date"
          value={nextRenewalDate}
          onChange={(e) => setNextRenewalDate(e.target.value)}
          min={todayStr()}
          className={`w-full rounded-lg border px-3.5 py-2.5 text-sm text-slate-100
            bg-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500
            ${errors.nextRenewalDate ? 'border-red-500' : 'border-slate-600'}`}
        />
        {errors.nextRenewalDate && (
          <p className="mt-1 text-xs text-red-500">{errors.nextRenewalDate}</p>
        )}
      </div>

      {/* Auto-renew + Reminder */}
      <div className="grid grid-cols-2 gap-3">
        {/* Auto-renew toggle */}
        <div>
          <label className="block text-sm font-medium text-slate-300 mb-1.5">Auto-renew</label>
          <button
            type="button"
            onClick={() => setAutoRenew((v) => !v)}
            className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors
              focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-indigo-500
              focus-visible:ring-offset-2 focus-visible:ring-offset-slate-900 ${autoRenew ? 'bg-indigo-600' : 'bg-slate-700'}`}
            role="switch"
            aria-checked={autoRenew}
            aria-label="Toggle auto-renew"
          >
            <span
              className={`inline-block h-4 w-4 rounded-full bg-white shadow transition-transform
                ${autoRenew ? 'translate-x-6' : 'translate-x-1'}`}
            />
          </button>
          <p className="mt-1 text-xs text-slate-400">
            {autoRenew ? 'Renews automatically' : 'Manual renewal'}
          </p>
        </div>

        {/* Reminder days */}
        <div>
          <label className="block text-sm font-medium text-slate-300 mb-1.5">
            Remind me (days before)
          </label>
          <input
            type="number"
            value={reminderDays}
            onChange={(e) => setReminderDays(e.target.value)}
            min="0"
            max="90"
            placeholder="0 = off"
            className={`w-full rounded-lg border px-3.5 py-2.5 text-sm text-slate-100
              bg-slate-800 placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-indigo-500
              ${errors.reminderDays ? 'border-red-500' : 'border-slate-600'}`}
          />
          {errors.reminderDays && (
            <p className="mt-1 text-xs text-red-500">{errors.reminderDays}</p>
          )}
        </div>
      </div>

      {/* Color picker */}
      <div>
        <label className="block text-sm font-medium text-slate-300 mb-1.5">Accent Color</label>
        <div className="flex flex-wrap gap-2">
          {PRESET_COLORS.map((c) => (
            <button
              key={c}
              type="button"
              onClick={() => setColor(c)}
              style={{ backgroundColor: c }}
              className={`w-7 h-7 rounded-full transition-all focus-visible:outline-none
                focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-indigo-500
                ${color === c ? 'ring-2 ring-offset-2 ring-white scale-110' : 'hover:scale-105'}`}
              aria-label={`Select color ${c}`}
              aria-pressed={color === c}
            />
          ))}
        </div>
      </div>

      {/* Notes */}
      <div>
        <label className="block text-sm font-medium text-slate-300 mb-1.5">
          Notes <span className="text-slate-500 font-normal">(optional)</span>
        </label>
        <textarea
          value={notes}
          onChange={(e) => setNotes(e.target.value)}
          rows={2}
          placeholder="Account info, notes..."
          className="w-full rounded-lg border border-slate-600 px-3.5 py-2.5 text-sm
            text-slate-100 bg-slate-800 placeholder:text-slate-500 focus:outline-none focus:ring-2
            focus:ring-indigo-500 resize-none"
        />
      </div>

      {/* Actions */}
      <div className="flex justify-end gap-3 pt-1 pb-1">
        <Button type="button" variant="secondary" onClick={onDone}>
          Cancel
        </Button>
        <Button type="submit" variant="primary">
          {isEdit ? 'Save Changes' : 'Add Subscription'}
        </Button>
      </div>
    </form>
  );
}
