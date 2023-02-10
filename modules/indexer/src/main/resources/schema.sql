create sequence if not exists pool_seq;
create sequence if not exists lm_pool_seq;
create sequence if not exists swaps_seq;
create sequence if not exists redeems_seq;
create sequence if not exists deposits_seq;
create sequence if not exists lm_deposits_seq;
create sequence if not exists compound_seq;

create domain public.hash32type as varchar(64);

create domain public.pubkey as varchar(66);

create domain public.ticker as varchar;

create table if not exists public.pools
(
    id               bigint            not null default nextval('pool_seq'),
    pool_state_id    public.hash32type primary key,
    pool_id          public.hash32type not null,
    lp_id            public.hash32type not null,
    lp_amount        bigint            not null,
    x_id             public.hash32type not null,
    x_amount         bigint            not null,
    y_id             public.hash32type not null,
    y_amount         bigint            not null,
    fee_num          integer           not null,
    timestamp        bigint            not null,
    height           bigint            not null,
    protocol_version integer           not null
);

alter table public.pools
    owner to ergo_admin;

create index pools__pool_id on public.pools using btree (pool_id);
create index pools__protocol_version on public.pools using btree (protocol_version);
create index pools__x_id on public.pools using btree (x_id);
create index pools__y_id on public.pools using btree (y_id);
create index pools__height on public.pools using btree (height);


CREATE TABLE IF NOT EXISTS public.swaps
(
    id                               bigint            not null default nextval('swaps_seq'),
    order_id                         public.hash32type PRIMARY KEY,
    pool_id                          public.hash32type NOT NULL,
    pool_state_id                    public.hash32type,
    max_miner_fee                    bigint,
    base_id                          public.hash32type NOT NULL,
    base_amount                      bigint            NOT NULL,
    min_quote_id                     public.hash32type NOT NULL,
    min_quote_amount                 bigint            NOT NULL,
    quote_amount                     bigint,
    dex_fee_per_token_num            bigint            NOT NULL,
    dex_fee_per_token_denom          bigint            NOT NULL,
    dex_fee                          bigint,
    fee_type                         text,
    redeemer                         public.pubkey,
    protocol_version                 integer           NOT NULL,
    contract_version                 text              NOT NULL,
    redeemer_ergo_tree               text,
    registered_transaction_id        public.hash32type NOT NULL,
    registered_transaction_timestamp bigint            NOT NULL,
    executed_transaction_id          public.hash32type,
    executed_transaction_timestamp   bigint,
    refunded_transaction_id          public.hash32type,
    refunded_transaction_timestamp   bigint
);

alter table public.swaps
    owner to ergo_admin;

create index swaps__pool_id on public.swaps using btree (pool_id);
create index swaps__pool_state_id on public.swaps using btree (pool_state_id);
create index swaps__protocol_version on public.swaps using btree (protocol_version);
create index swaps__base_id on public.swaps using btree (base_id);
create index swaps__min_quote_id on public.swaps using btree (min_quote_id);
create index swaps__register_timestamp on public.swaps using btree (registered_transaction_timestamp);

create table if not exists public.redeems
(
    id                               bigint            not null default nextval('redeems_seq'),
    order_id                         public.hash32type primary key,
    pool_id                          public.hash32type not null,
    pool_state_id                    public.hash32type,
    max_miner_fee                    bigint,
    lp_id                            public.hash32type not null,
    lp_amount                        bigint            not null,
    output_id_x                      public.hash32type,
    output_amount_x                  bigint,
    output_id_y                      public.hash32type,
    output_amount_y                  bigint,
    dex_fee                          bigint            not null,
    fee_type                         text              not null,
    redeemer                         public.pubkey     not null,
    protocol_version                 integer           not null,
    contract_version                 text              not null,
    redeemer_ergo_tree               text,
    registered_transaction_id        public.hash32type NOT NULL,
    registered_transaction_timestamp bigint            not null,
    executed_transaction_id          public.hash32type,
    executed_transaction_timestamp   bigint,
    refunded_transaction_id          public.hash32type,
    refunded_transaction_timestamp   bigint
);

alter table public.redeems
    owner to ergo_admin;

create index redeems__pool_id on public.redeems using btree (pool_id);
create index redeems__pool_state_id on public.redeems using btree (pool_state_id);
create index redeems__protocol_version on public.redeems using btree (protocol_version);
create index redeems__lp_id on public.redeems using btree (lp_id);
create index redeems__register_timestamp on public.redeems using btree (registered_transaction_timestamp);

create table if not exists public.deposits
(
    id                               bigint            not null default nextval('deposits_seq'),
    order_id                         public.hash32type primary key,
    pool_id                          public.hash32type not null,
    pool_state_id                    public.hash32type,
    max_miner_fee                    bigint,
    input_id_x                       public.hash32type not null,
    input_amount_x                   bigint            not null,
    input_id_y                       public.hash32type not null,
    input_amount_y                   bigint            not null,
    output_id_lp                     public.hash32type,
    output_amount_lp                 bigint,
    actual_input_amount_x            bigint,
    actual_input_amount_y            bigint,
    dex_fee                          bigint            not null,
    fee_type                         text              not null,
    redeemer                         public.pubkey     not null,
    protocol_version                 integer           not null,
    contract_version                 text              not null,
    redeemer_ergo_tree               text,
    registered_transaction_id        public.hash32type NOT NULL,
    registered_transaction_timestamp bigint            not null,
    executed_transaction_id          public.hash32type,
    executed_transaction_timestamp   bigint,
    refunded_transaction_id          public.hash32type,
    refunded_transaction_timestamp   bigint
);

alter table public.deposits
    owner to ergo_admin;

create index deposits__pool_id on public.deposits using btree (pool_id);
create index deposits__pool_state_id on public.deposits using btree (pool_state_id);
create index deposits__protocol_version on public.deposits using btree (protocol_version);
create index deposits__input_id_x on public.deposits using btree (input_id_x);
create index deposits__input_id_y on public.deposits using btree (input_id_y);
create index deposits__register_timestamp on public.deposits using btree (registered_transaction_timestamp);

create table if not exists public.lq_locks
(
    order_id                  public.hash32type primary key,
    transaction_id            public.hash32type not null,
    timestamp                 bigint            not null,
    deadline                  integer           not null,
    token_id                  public.hash32type not null,
    amount                    bigint            not null,
    redeemer                  public.pubkey     not null,
    contract_version          text              not null,
    evaluation_transaction_id public.hash32type,
    evaluation_lock_type      text
);

alter table public.lq_locks
    owner to ergo_admin;

create index lq_locks__asset_id on public.lq_locks using btree (token_id);
create index lq_locks__timestamp on public.lq_locks using btree (timestamp);

create table if not exists public.off_chain_fee
(
    pool_id   public.hash32type not null,
    order_id  public.hash32type not null,
    output_id public.hash32type primary key,
    pub_key   public.pubkey     not null,
    dex_fee   bigint            not null,
    fee_type  text              not null
);

alter table public.off_chain_fee
    owner to ergo_admin;

create index off_chain_fee__pub_key on public.off_chain_fee using btree (pub_key);

create table if not exists public.assets
(
    id       public.hash32type primary key,
    ticker   public.ticker,
    decimals integer
);

alter table public.assets
    owner to ergo_admin;

create index assets__ticker on public.assets using btree (ticker);

create table if not exists public.blocks
(
    id        public.hash32type primary key,
    height    integer,
    timestamp bigint
);

alter table public.blocks
    owner to ergo_admin;

create index blocks__height on public.blocks using btree (height);
create index blocks__id on public.blocks using btree (id);

create table if not exists public.lm_pools
(
    id                  bigint            not null default nextval('lm_pool_seq'),
    pool_state_id       public.hash32type primary key,
    pool_id             public.hash32type not null,
    reward_id           public.hash32type not null,
    reward_amount       bigint            not null,
    lq_id               public.hash32type not null,
    lq_amount           bigint            not null,
    v_lq_id             public.hash32type not null,
    v_lq_amount         bigint            not null,
    tmp_id              public.hash32type not null,
    tmp_amount          bigint            not null,
    epoch_length        integer           not null,
    epochs_num          integer           not null,
    program_start       integer           not null,
    redeem_blocks_delta integer           not null,
    program_budget      integer           not null,
    max_rounding_error  integer           not null,
    execution_budget    integer,
    epoch_index         integer,
    timestamp           bigint            not null,
    version             text              not null,
    height              bigint            not null,
    protocol_version    integer           not null
);

alter table public.lm_pools
    owner to ergo_admin;

create index lm_pools__pool_id on public.lm_pools using btree (pool_id);
create index lm_pools__protocol_version on public.lm_pools using btree (protocol_version);
create index lm_pools__reward_id on public.lm_pools using btree (reward_id);
create index lm_pools__lq_id on public.lm_pools using btree (lq_id);
create index lm_pools__v_lq_id on public.lm_pools using btree (v_lq_id);
create index lm_pools__timestamp on public.lm_pools using btree (timestamp);
create index lm_pools__height on public.lm_pools using btree (height);

create table if not exists public.lm_compound
(
    id                               bigint            not null default nextval('compound_seq'),
    order_id                         public.hash32type primary key,
    pool_id                          public.hash32type not null,
    pool_state_id                    public.hash32type,
    v_lq_id                          public.hash32type not null,
    v_lq_amount                      bigint            not null,
    tmp_id                           public.hash32type not null,
    tmp_amount                       bigint            not null,
    bundle_key_id                    public.hash32type not null,
    redeemer                         public.pubkey     not null,
    version                          text              not null,
    protocol_version                 integer           not null,
    registered_transaction_id        public.hash32type NOT NULL,
    registered_transaction_timestamp bigint            not null,
    executed_transaction_id          public.hash32type,
    executed_transaction_timestamp   bigint
);

alter table public.lm_compound
    owner to ergo_admin;

create index lm_compound__pool_id on public.lm_compound using btree (pool_id);
create index lm_compound__pool_state_id on public.lm_compound using btree (pool_state_id);
create index lm_compound__protocol_version on public.lm_compound using btree (protocol_version);
create index lm_compound__input_id on public.lm_compound using btree (v_lq_id);
create index lm_compound__lp_id on public.lm_compound using btree (bundle_key_id);
create index lm_compound__tmp_id on public.lm_compound using btree (tmp_id);
create index lm_compound__register_timestamp on public.lm_compound using btree (registered_transaction_timestamp);

create table if not exists public.lm_deposits
(
    order_id                         public.hash32type primary key,
    pool_id                          public.hash32type not null,
    pool_state_id                    public.hash32type,
    max_miner_fee                    bigint            not null,
    expected_num_epochs              integer           not null,
    input_id                         public.hash32type not null,
    input_amount                     bigint            not null,
    lp_id                            public.hash32type,
    lp_amount                        bigint,
    compound_id                      public.hash32type,
    protocol_version                 integer           not null,
    contract_version                 text              not null,
    redeemer_ergo_tree               text              not null,
    registered_transaction_id        public.hash32type NOT NULL,
    registered_transaction_timestamp bigint            not null,
    executed_transaction_id          public.hash32type,
    executed_transaction_timestamp   bigint,
    refunded_transaction_id          public.hash32type,
    refunded_transaction_timestamp   bigint
);

alter table public.lm_deposits
    owner to ergo_admin;

create index lm_deposits__pool_id on public.lm_deposits using btree (pool_id);
create index lm_deposits__pool_state_id on public.lm_deposits using btree (pool_state_id);
create index lm_deposits__protocol_version on public.lm_deposits using btree (protocol_version);
create index lm_deposits__input_id on public.lm_deposits using btree (input_id);
create index lm_deposits__lp_id on public.lm_deposits using btree (lp_id);
create index lm_deposits__register_timestamp on public.lm_deposits using btree (registered_transaction_timestamp);
create index lm_deposits__compound_id on public.lm_deposits using btree (compound_id);

create table if not exists public.lm_redeems
(
    order_id                         public.hash32type primary key,
    pool_id                          public.hash32type,
    pool_state_id                    public.hash32type,
    bundle_key_id                    public.hash32type not null,
    expected_lq_id                   public.hash32type not null,
    expected_lq_amount               bigint            not null,
    max_miner_fee                    bigint            not null,
    redeemer_ergo_tree               text              not null,
    out_id                           public.hash32type,
    out_amount                       bigint,
    box_id                           public.hash32type,
    contract_version                 text              not null,
    protocol_version                 integer           not null,
    registered_transaction_id        public.hash32type NOT NULL,
    registered_transaction_timestamp bigint            not null,
    executed_transaction_id          public.hash32type,
    executed_transaction_timestamp   bigint,
    refunded_transaction_id          public.hash32type,
    refunded_transaction_timestamp   bigint
);

