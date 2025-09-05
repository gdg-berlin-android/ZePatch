package de.berlindroid.zepatch

import de.berlindroid.zepatch.generated.PatchRegistry

/**
 * It's like Dagger, but way worse.
 *
 * A (hopefully temporary) dependency injection framework, with no runtime cost, cause everything is
 * hand-witten.
 */

// Generated via @Patch + KSP

val patchables = PatchRegistry.patchables
