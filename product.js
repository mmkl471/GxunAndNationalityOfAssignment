import request from '../utils/request'

export function getProducts(params) {
  return request({
    url: '/products',
    method: 'get',
    params
  })
}

export function getProductById(id) {
  return request({
    url: `/products/${id}`,
    method: 'get'
  })
}
