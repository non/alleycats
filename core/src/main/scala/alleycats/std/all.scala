package alleycats
package std

import export._

@reexports(
  EmptyKInstances,
  ListInstances,
  OptionInstances,
  SetInstances,
  TryInstances
) object all extends LegacySetInstances with LegacyTryInstances
