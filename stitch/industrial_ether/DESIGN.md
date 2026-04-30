# Design System Specification: Industrial Sophistication

## 1. Overview & Creative North Star: "The Architectural Pulse"
The creative North Star for this design system is **The Architectural Pulse**. In an enterprise factory management context, "modern" often mistakenly translates to "cold." We are breaking that trope. This system treats industrial data not as a static spreadsheet, but as a living, breathing organism. 

We move beyond the "template" look by utilizing **Soft-UI Neumorphism**—not the dated, heavy-handed version, but a refined, high-end "Editorial" approach. We utilize intentional asymmetry, allowing heavy data visualizations to be balanced by expansive white space. By layering surfaces like stacked sheets of frosted glass, we create a UI that feels deep, tactile, and authoritative, yet remarkably effortless.

---

## 2. Colors & Surface Philosophy
Color in this system is used to define "State" and "Soul" rather than structure. We rely on the interplay of light and tone to guide the eye.

### The Palette
*   **Primary (`#2b54bf`):** Our "Command Blue." Use for high-intent actions and primary branding.
*   **Secondary (`#006d37`):** "Success/Profit." Reserved for growth metrics and positive operational status.
*   **Tertiary (`#80409b`):** "Lavender Insight." Used for deep-analytics, machine learning insights, or secondary categorizations.
*   **Error (`#ba1a1a`):** "Soft Crimson." High-visibility alerts that demand immediate intervention.

### The "No-Line" Rule
**Borders are prohibited for sectioning.** 1px solid lines create visual "noise" that clutters enterprise dashboards. Boundaries must be defined strictly through:
1.  **Background Color Shifts:** A `surface-container-low` card resting on a `surface` background.
2.  **Tonal Transitions:** Using subtle shifts in the neutral scale to indicate where one functional area ends and another begins.

### The "Glass & Gradient" Rule
To elevate the aesthetic from "software" to "experience," primary CTAs and Hero backgrounds should utilize subtle gradients (e.g., `primary` to `primary_container`). For floating panels—such as slide-outs or modals—apply **Glassmorphism**: use semi-transparent surface colors with a `20px` to `40px` backdrop blur to allow the factory’s "pulse" (the background data) to bleed through.

---

## 3. Typography: Editorial Authority
We pair **Manrope** (Display/Headlines) with **Inter** (Body/Labels) to create a high-contrast, editorial hierarchy.

*   **Display & Headline (Manrope):** These are your "Anchors." Large, bold, and airy. They provide the "Trustworthy" weight required for an enterprise system.
*   **Body & Labels (Inter):** These are your "Engine." Focused on maximum legibility at small scales. Inter’s tall x-height ensures that complex factory telemetry remains readable in high-stress environments.

**Hierarchy Goal:** A user should be able to squint at the screen and still understand the hierarchy of information based solely on the scale and weight of the type.

---

## 4. Elevation & Depth: Tonal Layering
Traditional shadows are replaced with **Tonal Layering**. We treat the screen as a 3D space where importance is determined by "Physical Height."

### The Layering Principle
Depth is achieved by stacking `surface-container` tiers:
*   **Base:** `surface` (`#f7f9fc`)
*   **Sectioning:** `surface-container-low`
*   **Interactive Cards:** `surface-container-lowest` (pure white `#ffffff`) to create a soft, natural lift.

### Ambient Shadows
When a component must "float" (e.g., a critical alert or a floating action button), use **Ambient Shadows**:
*   **Blur:** `24px` to `48px`.
*   **Opacity:** `4%` to `8%`.
*   **Color:** Use a tinted version of `on-surface` (dark blue-grey) rather than black. This mimics natural light bouncing off the factory floor.

### The "Ghost Border" Fallback
If a boundary is required for accessibility, use a **Ghost Border**: the `outline-variant` token at **15% opacity**. It provides a suggestion of a container without breaking the Soft-UI flow.

---

## 5. Components: Soft-UI Primitives

### Cards & Panes
*   **Radius:** Always `xl` (`3rem`) for main containers; `lg` (`2rem`) for internal cards.
*   **Structure:** Forbid divider lines. Separate content using **Vertical White Space** (using the `8` or `10` spacing scale) or subtle background shifts.

### Buttons
*   **Primary:** Subtle gradient from `primary` to `primary_container`. `24px` corner radius. Soft `primary` tinted shadow.
*   **Secondary:** `surface-container-high` background with `on-surface` text. No border.
*   **State:** On hover, increase the "glow" (shadow spread) rather than darkening the color.

### Input Fields
*   **Aesthetic:** Minimalist. Use `surface-container-highest` as a subtle fill. 
*   **Interaction:** On focus, the field should transition to a `primary` "Ghost Border" and a very soft inner shadow to look "pressed" into the interface.

### Chips & Badges
*   **Status:** Use `secondary_container` for "Active" and `error_container` for "Halt." 
*   **Style:** Pill-shaped (`full` radius). Type should be `label-md` for maximum density.

### Factory-Specific Components
*   **Machine Health Gauges:** Use wide-track circular progress bars with `secondary` (Emerald) for health. Avoid thin lines; use thick, rounded strokes (`12px+`).
*   **Timeline Scrubber:** For viewing machine logs. Use a `surface-container-low` track with a `primary` handle.

---

## 6. Do’s and Don’ts

### Do
*   **DO** use whitespace as a functional tool. If a dashboard feels crowded, increase the spacing scale rather than shrinking the text.
*   **DO** use "surface-nesting" to group related machine data.
*   **DO** ensure that the `24px` (1.5rem) corner radius is consistent across all main UI elements to maintain the "Soft" signature.

### Don’t
*   **DON'T** use pure black (`#000000`) for text or shadows. It breaks the organic, premium feel.
*   **DON'T** use 1px dividers to separate list items. Use a `0.5rem` gap and a subtle `surface` color shift on hover.
*   **DON'T** use "Legacy Blue." Stay strictly within the defined `primary` (`#2b54bf`) range to avoid the "Java Swing" or old-school enterprise look.