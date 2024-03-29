create domain hash32type as varchar(64);

create domain pubkey as varchar(66);

create domain ticker as varchar;

create table if not exists pools
(
    pool_state_id    hash32type primary key,
    pool_id          hash32type not null,
    lp_id            hash32type not null,
    lp_amount        bigint     not null,
    x_id             hash32type not null,
    x_amount         bigint     not null,
    y_id             hash32type not null,
    y_amount         bigint     not null,
    fee_num          integer    not null,
    timestamp        bigint     not null,
    height           bigint     not null,
    protocol_version integer    not null
);

create index pools__pool_id on pools using btree (pool_id);
create index pools__protocol_version on pools using btree (protocol_version);
create index pools__x_id on pools using btree (x_id);
create index pools__y_id on pools using btree (y_id);

CREATE TABLE IF NOT EXISTS swaps
(
    order_id                         hash32type PRIMARY KEY,
    pool_id                          hash32type NOT NULL,
    pool_state_id                    hash32type,
    max_miner_fee                    bigint,
    base_id                          hash32type NOT NULL,
    base_amount                      bigint     NOT NULL,
    min_quote_id                     hash32type NOT NULL,
    min_quote_amount                 bigint     NOT NULL,
    quote_amount                     bigint,
    dex_fee_per_token_num            bigint     NOT NULL,
    dex_fee_per_token_denom          bigint     NOT NULL,
    dex_fee                          bigint,
    fee_type                         text,
    redeemer                         pubkey,
    protocol_version                 integer    NOT NULL,
    contract_version                 text       NOT NULL,
    redeemer_ergo_tree               text,
    registered_transaction_id        hash32type NOT NULL,
    registered_transaction_timestamp bigint     NOT NULL,
    executed_transaction_id          hash32type,
    executed_transaction_timestamp   bigint,
    refunded_transaction_id          hash32type,
    refunded_transaction_timestamp   bigint
);

create index swaps__pool_id on swaps using btree (pool_id);
create index swaps__pool_state_id on swaps using btree (pool_state_id);
create index swaps__protocol_version on swaps using btree (protocol_version);
create index swaps__base_id on swaps using btree (base_id);
create index swaps__min_quote_id on swaps using btree (min_quote_id);

create table if not exists redeems
(
    order_id                         hash32type primary key,
    pool_id                          hash32type not null,
    pool_state_id                    hash32type,
    max_miner_fee                    bigint,
    lp_id                            hash32type not null,
    lp_amount                        bigint     not null,
    output_id_x                      hash32type,
    output_amount_x                  bigint,
    output_id_y                      hash32type,
    output_amount_y                  bigint,
    dex_fee                          bigint     not null,
    fee_type                         text       not null,
    redeemer                         pubkey     not null,
    protocol_version                 integer    not null,
    contract_version                 text       not null,
    redeemer_ergo_tree               text,
    registered_transaction_id        hash32type NOT NULL,
    registered_transaction_timestamp bigint     not null,
    executed_transaction_id          hash32type,
    executed_transaction_timestamp   bigint,
    refunded_transaction_id          hash32type,
    refunded_transaction_timestamp   bigint
);

create index redeems__pool_id on redeems using btree (pool_id);
create index redeems__pool_state_id on redeems using btree (pool_state_id);
create index redeems__protocol_version on redeems using btree (protocol_version);
create index redeems__lp_id on redeems using btree (lp_id);

create table if not exists deposits
(
    order_id                         hash32type primary key,
    pool_id                          hash32type not null,
    pool_state_id                    hash32type,
    max_miner_fee                    bigint,
    input_id_x                       hash32type not null,
    input_amount_x                   bigint     not null,
    input_id_y                       hash32type not null,
    input_amount_y                   bigint     not null,
    output_id_lp                     hash32type,
    output_amount_lp                 bigint,
    actual_input_amount_x            bigint,
    actual_input_amount_y            bigint,
    dex_fee                          bigint     not null,
    fee_type                         text       not null,
    redeemer                         pubkey     not null,
    protocol_version                 integer    not null,
    contract_version                 text       not null,
    redeemer_ergo_tree               text,
    registered_transaction_id        hash32type NOT NULL,
    registered_transaction_timestamp bigint     not null,
    executed_transaction_id          hash32type,
    executed_transaction_timestamp   bigint,
    refunded_transaction_id          hash32type,
    refunded_transaction_timestamp   bigint
);

create index deposits__pool_id on deposits using btree (pool_id);
create index deposits__pool_state_id on deposits using btree (pool_state_id);
create index deposits__protocol_version on deposits using btree (protocol_version);
create index deposits__input_id_x on deposits using btree (input_id_x);
create index deposits__input_id_y on deposits using btree (input_id_y);

create table if not exists lq_locks
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

create index lq_locks__asset_id on lq_locks using btree (token_id);

create table if not exists off_chain_fee
(
    pool_id   hash32type not null,
    order_id  hash32type not null,
    output_id hash32type primary key,
    pub_key   pubkey     not null,
    dex_fee   bigint     not null,
    fee_type  text       not null
);

create index off_chain_fee__pub_key on off_chain_fee using btree (pub_key);

create table if not exists assets
(
    id       hash32type primary key,
    ticker   ticker,
    decimals integer
);

create index assets__ticker on assets using btree (ticker);

create table if not exists public.lm_pools
(
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

create table if not exists public.lm_compound
(
    order_id                         public.hash32type primary key,
    pool_id                          public.hash32type not null,
    pool_state_id                    public.hash32type,
    v_lq_id                          public.hash32type not null,
    v_lq_amount                      bigint            not null,
    tmp_id                           public.hash32type not null,
    tmp_amount                       bigint            not null,
    interest_id                      public.hash32type,
    interest_amount                  bigint,
    bundle_key_id                    public.hash32type not null,
    redeemer                         public.pubkey     not null,
    version                          text              not null,
    protocol_version                 integer           not null,
    registered_transaction_id        public.hash32type NOT NULL,
    registered_transaction_timestamp bigint            not null,
    executed_transaction_id          public.hash32type,
    executed_transaction_timestamp   bigint
);

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

create table if not exists public.lm_redeems
(
    order_id                         public.hash32type primary key,
    pool_id                          public.hash32type,
    pool_state_id                    public.hash32type,
    max_miner_fee                    bigint            not null,
    bundle_key_id                    public.hash32type not null,
    expected_lq_id                   public.hash32type not null,
    expected_lq_amount               bigint            not null,
    redeemer_ergo_tree               text              not null,
    out_id                           public.hash32type,
    out_amount                       bigint,
    out_box_id                       public.hash32type,
    protocol_version                 integer           not null,
    contract_version                 text              not null,
    registered_transaction_id        public.hash32type NOT NULL,
    registered_transaction_timestamp bigint            not null,
    executed_transaction_id          public.hash32type,
    executed_transaction_timestamp   bigint,
    refunded_transaction_id          public.hash32type,
    refunded_transaction_timestamp   bigint
);
