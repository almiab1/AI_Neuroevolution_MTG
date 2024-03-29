[
    new MagicStatic(MagicLayer.ModPT, CREATURE) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            int amount = 0;
            final Set<MagicSubType> self = permanent.getSubTypes();
            self.retainAll(MagicSubType.ALL_CREATURES);
            CREATURE.except(permanent).filter(source) each {
                for (final MagicSubType subType : self) {
                    if (it.hasSubType(subType)) {
                        amount += 1;
                        break;
                    }
                }
            }
            pt.add(amount,amount);
        }
    }
]
