export const JAVA_ENDPOINTS = {
  "manufacturer": "/v1/equipment/manufacturers",
  "equipmentCategory": "/v1/equipment/categories",
  "equipmentAssets": "/v1/equipment/assets",
  "checkPlan": "/v1/inspection/plans",
  "checkItem": "/v1/inspection/items",
  "checkRecord": "/v1/inspection/records",
  "maintenancePlan": "/v1/maintenance/plans",
  "maintenanceItem": "/v1/maintenance/items",
  "maintenanceRecord": "/v1/maintenance/records",
  "repairApply": "/v1/repair/applies",
  "accessoryApply": "/v1/accessory/applies",
  "user": "/v1/system/users"
};
function endpoint(pageKey) {
  if (!JAVA_ENDPOINTS[pageKey]) throw new Error('未配置页面接口：' + pageKey);
  return '/java-api' + JAVA_ENDPOINTS[pageKey];
}
async function request(url, options = {}) {
  const response = await fetch(url, { ...options, headers: { 'Content-Type': 'application/json', ...options.headers } });
  const payload = await response.json().catch(() => null);
  if (!response.ok || !payload?.success) throw new Error(payload?.message || '请求失败 (' + response.status + ')');
  return payload.data;
}
export async function fetchCrudRows(pageKey) {
  const page = await request(endpoint(pageKey));
  return page.records;
}
export function createCrudRow(pageKey, payload) {
  return request(endpoint(pageKey), { method: 'POST', body: JSON.stringify(payload) });
}
export function updateCrudRow(pageKey, id, payload) {
  return request(endpoint(pageKey) + '/' + encodeURIComponent(id), { method: 'PUT', body: JSON.stringify(payload) });
}
export function deleteCrudRow(pageKey, id) {
  return request(endpoint(pageKey) + '/' + encodeURIComponent(id), { method: 'DELETE' });
}
