create domain public.hash32type as varchar(64);

create domain public.pubkey as varchar(66);

create domain public.ticker as varchar;

create table if not exists public.pools (
    pool_state_id public.hash32type primary key,
    pool_id public.hash32type not null,
    lp_id public.hash32type not null,
    lp_amount bigint not null,
    x_id public.hash32type not null,
    x_amount bigint not null,
    y_id public.hash32type not null,
    y_amount bigint not null,
    fee_num integer not null,
    timestamp bigint not null,
    height bigint not null,
    protocol_version integer not null
);

CREATE TABLE IF NOT EXISTS public.swaps (
    order_id public.hash32type PRIMARY KEY,
    pool_id public.hash32type NOT NULL,
    pool_state_id public.hash32type,
    max_miner_fee bigint,
    base_id public.hash32type NOT NULL,
    base_amount bigint NOT NULL,
    min_quote_id public.hash32type NOT NULL,
    min_quote_amount bigint NOT NULL,
    quote_amount bigint,
    dex_fee_per_token_num bigint NOT NULL,
    dex_fee_per_token_denom bigint NOT NULL,
    redeemer public.pubkey,
    protocol_version integer NOT NULL,
    contract_version integer NOT NULL,
    redeemer_ergo_tree text,
    registered_transaction_id public.hash32type,
    registered_transaction_timestamp bigint NOT NULL,
    executed_transaction_id public.hash32type,
    executed_transaction_timestamp bigint,
    refunded_transaction_id public.hash32type,
    refunded_transaction_timestamp bigint
);

create table if not exists public.redeems (
    order_id public.hash32type primary key,
    pool_id public.hash32type not null,
    pool_state_id public.hash32type,
    max_miner_fee bigint,
    lp_id public.hash32type not null,
    lp_amount bigint not null,
    output_id_x public.hash32type,
    output_amount_x bigint,
    output_id_y public.hash32type,
    output_amount_y bigint,
    dex_fee bigint not null,
    fee_type text not null,
    redeemer public.pubkey not null,
    protocol_version integer not null,
    contract_version integer not null,
    registered_transaction_id public.hash32type,
    registered_transaction_timestamp bigint not null,
    executed_transaction_id public.hash32type,
    executed_transaction_timestamp bigint,
    refunded_transaction_id public.hash32type,
    refunded_transaction_timestamp bigint
);

create table if not exists public.deposits (
    order_id public.hash32type primary key,
    pool_id public.hash32type not null,
    pool_state_id public.hash32type,
    max_miner_fee bigint,
    input_id_x public.hash32type not null,
    input_amount_x bigint not null,
    input_id_y public.hash32type not null,
    input_amount_y bigint not null,
    output_id_lp public.hash32type,
    output_amount_lp bigint,
    dex_fee bigint not null,
    fee_type text not null,
    redeemer public.pubkey not null,
    protocol_version integer not null,
    contract_version integer not null,
    registered_transaction_id public.hash32type,
    registered_transaction_timestamp bigint not null,
    executed_transaction_id public.hash32type,
    executed_transaction_timestamp bigint,
    refunded_transaction_id public.hash32type,
    refunded_transaction_timestamp bigint
);

create table if not exists public.lq_locks (
    order_id public.hash32type primary key,
    deadline integer not null,
    token_id public.hash32type not null,
    amount bigint not null,
    redeemer public.pubkey not null,
    contract_version integer not null
);

create table if not exists public.off_chain_fee (
    pool_id public.hash32type not null,
    order_id public.hash32type not null,
    output_id public.hash32type primary key,
    pub_key public.pubkey not null,
    dex_fee bigint not null,
    fee_type text not null
);