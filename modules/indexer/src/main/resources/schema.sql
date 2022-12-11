create domain public.hash32type as varchar(64);

create domain public.pubkey as varchar(66);

create domain public.address as varchar(64);

create domain public.ticker as varchar;

create table if not exists public.swaps (
    order_id public.hash32type primary key,
    pool_id public.hash32type not null,
    pool_box_id public.hash32type,
    max_miner_fee bigint,
    input_id public.hash32type not null,
    input_value bigint not null,
    min_output_id public.hash32type not null,
    min_output_amount bigint not null,
    output_amount bigint,
    dex_fee_per_token_num bigint not null,
    dex_fee_per_token_denom bigint not null,
    redeemer public.pubkey,
    protocol_version integer not null,
    contract_version integer not null,
    redeemer_ergo_tree text,
    registered_transaction_id public.hash32type,
    registered_transaction_timestamp bigint not null,
    executed_transaction_id public.hash32type,
    executed_transaction_timestamp bigint,
    refunded_transaction_id public.hash32type,
    refunded_transaction_timestamp bigint
);

create table if not exists public.redeems (
    order_id public.hash32type primary key,
    pool_id public.hash32type not null,
    pool_box_id public.hash32type,
    max_miner_fee bigint,
    lp_id public.hash32type not null,
    lp_amount bigint not null,
    output_amount_x bigint,
    output_id_x public.hash32type,
    output_amount_y bigint,
    output_id_y public.hash32type,
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